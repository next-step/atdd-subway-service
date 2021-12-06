package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTest.*;

public class AuthAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("Bearer Token 로그인 요청")
    @Test
    void myInfoWithBearerAuth() {

        // when
        ExtractableResponse<Response> response = ACCESS_TOKEN_요청(EMAIL, PASSWORD);

        // then
        인증됨(response);
    }

    @DisplayName("Bearer Token 로그인 실패 - 존재하지 않는 이메일 주소")
    @Test
    void myInfoWithBadBearerAuthNotFoundEmail() {
        // when
        ExtractableResponse<Response> response = ACCESS_TOKEN_요청(NEW_EMAIL, NEW_PASSWORD);

        // then
        인증되지_않음(response);
    }

    @DisplayName("Bearer Token 로그인 실패 - 잘못된 비밀번호")
    @Test
    void myInfoWithBadBearerAuthWrongPassword() {
        // when
        ExtractableResponse<Response> response = ACCESS_TOKEN_요청(EMAIL, NEW_PASSWORD);

        // then
        인증되지_않음(response);
    }

    @DisplayName("Bearer Token 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // when
        ExtractableResponse<Response> response = 내_정보_조회_요청("Bad Token");

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 인증됨(ExtractableResponse<Response> response) {
        요청_결과_검증(response, HttpStatus.OK);
    }

    public static void 인증되지_않음(ExtractableResponse<Response> response) {
        요청_결과_검증(response, HttpStatus.UNAUTHORIZED);
    }

    public static ExtractableResponse<Response> ACCESS_TOKEN_요청(String email, String password) {
        TokenRequest request = new TokenRequest(email, password);

        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login/token")
                .then().log().all().extract();
    }

}
