package nextstep.subway.auth.acceptance;

import static nextstep.subway.AcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;

public class AuthAcceptanceMethods {
    public static final String LOCATION_HEADER_NAME = "Location";
    private static final String LOGIN_TOKEN_URL_PATH = "/login/token";

    private AuthAcceptanceMethods() {}

    public static ExtractableResponse<Response> 회원_로그인_요청(TokenRequest tokenRequest) {
        return post(LOGIN_TOKEN_URL_PATH, tokenRequest);
    }

    public static ExtractableResponse<Response> 회원_로그인_됨(TokenRequest tokenRequest) {
        return post(LOGIN_TOKEN_URL_PATH, tokenRequest);
    }

    public static void 회원_로그인_성공함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(TokenResponse.class).getAccessToken()).isNotBlank();
    }

    public static void 회원_로그인_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 토큰_인증_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
