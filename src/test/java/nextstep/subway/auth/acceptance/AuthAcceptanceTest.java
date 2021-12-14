package nextstep.subway.auth.acceptance;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AuthAcceptanceTest extends AcceptanceTest {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        ExtractableResponse<Response> createResponse = MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("로그인을 성공한다 ")
    @Test
    void loginTest() {
        // given
        TokenRequest param = new TokenRequest(EMAIL, PASSWORD);

        // when
        TokenResponse tokenResponse = 로그인을_실행한다(param);

        // then
        assertThat(tokenResponse.getAccessToken()).isNotNull();
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        TokenRequest param = new TokenRequest(EMAIL, PASSWORD);

        TokenResponse tokenResponse = 로그인을_실행한다(param);

        ExtractableResponse<Response> myInfoResponse = 내_정보를_조회(tokenResponse);

        내_정보가_조회됨(myInfoResponse);

    }

    public static void 내_정보가_조회됨(ExtractableResponse<Response> myInfoResponse) {
        assertThat(myInfoResponse.jsonPath().getString("email")).isEqualTo(EMAIL);
        assertThat(myInfoResponse.jsonPath().getInt("age")).isEqualTo(AGE);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        TokenRequest param = new TokenRequest(EMAIL, PASSWORD + " ");

        assertThatThrownBy(() -> {
            로그인을_실행한다(param);
        }).isInstanceOf(UnrecognizedPropertyException.class);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        TokenResponse tokenResponse = new TokenResponse("fakeToken");

        ExtractableResponse<Response> myInfoResponse = 내_정보를_조회(tokenResponse);

        assertThat(myInfoResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static TokenResponse 로그인을_실행한다(TokenRequest param) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .when().post("/login/token")
                .then().log().all()
                .extract().as(TokenResponse.class);
    }

    public static ExtractableResponse<Response> 내_정보를_조회(TokenResponse tokenResponse) {
        return RestAssured.given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }
}
