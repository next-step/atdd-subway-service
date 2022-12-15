package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.acceptance.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    private final String wrongPassword = "wrongPassword";
    private final String wrongEmail = "wrongEmail";

    @BeforeEach
    void setUpMember() {
        super.setUp();
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("등록된 정보로 로그인을 요청하면 로그인이 성공하고 accessToken 이 발급된다")
    @Test
    void login() {

        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        로그인이_성공한다(response);
    }

    @DisplayName("잘못된 Password 를 입력하면 로그인이 실패한다")
    @Test
    void loginWithWrongPassword() {

        ExtractableResponse<Response> response = 로그인_요청(EMAIL, wrongPassword);

        로그인이_실패한다(response);
    }

    @DisplayName("잘못된 eMail 을 입력하면 로그인이 실패한다")
    @Test
    void loginWithWrongEmail() {

        ExtractableResponse<Response> response = 로그인_요청(wrongEmail, PASSWORD);

        로그인이_실패한다(response);
    }

    @DisplayName("유효한 토큰으로 요청하면 요청이 성공한다")
    @Test
    void myInfoWithBearerAuth() {

        TokenResponse tokenResponse = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);

        ExtractableResponse<Response> 내정보 = 내정보_요청(tokenResponse.getAccessToken());

        내정보_요청_성공함(내정보, EMAIL);
    }

    @DisplayName("유효하지 않은 토큰으로 요청하면 요청이 실패한다")
    @Test
    void myInfoWithWrongBearerAuth() {
        final String wrongToken = "wrongToken";

        ExtractableResponse<Response> 내정보 = 내정보_요청(wrongToken);

        내정보_요청_실패함(내정보);
    }


    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(tokenRequest)
            .when().post("/login/token")
            .then().log().all()
            .extract();
    }

    private void 로그인이_성공한다(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("accessToken")).isNotEmpty();
    }

    private void 로그인이_실패한다(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
