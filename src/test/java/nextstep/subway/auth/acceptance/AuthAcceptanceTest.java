package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.나의_정보_조회_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String email = "email@email.com";
    private static final String password = "password";
    private static final int age = 20;
    private TokenRequest tokenRequest;

    @BeforeEach
    public void setUp() {
        super.setUp();

        //given
        회원_생성을_요청(email, password, age);

        tokenRequest = new TokenRequest(email, password);
    }

    @DisplayName("Bearer Auth")
    @Test
    void loginWithBearerAuth() {
        // when
        ExtractableResponse<Response> response = 토큰_요청(tokenRequest);

        // then
        토큰_응답됨(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void loginWithBadBearerAuth() {
        // when
        ExtractableResponse<Response> response = 토큰_요청(new TokenRequest("없는이메일", "password"));

        // then
        인증_싪패(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void loginWithWrongBearerAuth() {
        // when
        ExtractableResponse<Response> response = 나의_정보_조회_요청("InvalidToken");

        // then
        유효하지_않은_토큰(response);
    }

    private static ExtractableResponse<Response> 토큰_요청(TokenRequest params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all().
                extract();
    }

    private static void 토큰_응답됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 인증_싪패(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private static void 유효하지_않은_토큰(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
