package nextstep.subway.auth.acceptance;

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

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final int AGE = 20;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        // given 회원 등록되어 있음
        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // when 로그인 요청
        TokenRequest request = new TokenRequest(EMAIL, PASSWORD);
        ExtractableResponse<Response> response = 로그인_요청(request);

        // then 로그인 됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        TokenResponse tokenResponse = response.body().as(TokenResponse.class);
        assertThat(tokenResponse.getAccessToken()).isNotNull();
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // when 로그인 요청
        TokenRequest request = new TokenRequest(EMAIL, "wrongpassword");
        ExtractableResponse<Response> response = 로그인_요청(request);

        // then 로그인 됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // when
        ExtractableResponse<Response> response = MemberAcceptanceTest.나의_정보_조회("accesstoken");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private ExtractableResponse<Response> 로그인_요청(TokenRequest request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

}
