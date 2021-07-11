package nextstep.subway.auth.domain;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.AuthToken;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class AuthTestSnippet {

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

    public static void 로그인_성공_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(response.as(TokenResponse.class).getAccessToken()).isNotBlank();
    }

    public static void 로그인_실패_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    public static void 로그인_토큰_추출(ExtractableResponse<Response> response, AuthToken authToken) {
        authToken.changeToken(response.as(AuthToken.class).getAccessToken());
    }

    public static Executable 로그인_요청_및_성공_확인(String email, String password, AuthToken authToken) {
        return () -> {
            // when
            ExtractableResponse<Response> loginResponse = 로그인_요청(email, password);

            // then
            로그인_성공_확인(loginResponse);
            로그인_토큰_추출(loginResponse, authToken);
        };
    }

    public static Executable 로그인_요청_및_실패_확인(String email, String password) {
        return () -> {
            // when
            ExtractableResponse<Response> loginResponse = 로그인_요청(email, password);

            // then
            로그인_실패_확인(loginResponse);
        };
    }

    public static Executable 로그인_요청_및_성공_확인(String email, String password) {
        return () -> {
            // when
            ExtractableResponse<Response> loginResponse = 로그인_요청(email, password);

            // then
            로그인_성공_확인(loginResponse);
        };
    }
}
