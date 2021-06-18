package nextstep.subway.auth.acceptance;

import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.request.Given;
import nextstep.subway.request.When;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;

import static nextstep.subway.request.AcceptanceTestRequest.post;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceRequest {
    public static Executable 로그인_요청_성공됨(TokenRequest request, AuthToken authToken) {
        return () -> {
            TokenResponse tokenResponse = 로그인_요청_및_전체_검증(request);

            authToken.changeToken(tokenResponse.getAccessToken());
        };
    }
    
    public static Executable 로그인_요청_성공됨(TokenRequest request) {
        return () -> {
            로그인_요청_및_전체_검증(request);
        };
    }

    public static Executable 로그인_요청_실패함(TokenRequest request) {
        return () -> {
            ExtractableResponse<Response> response = 로그인_요청(request);

            로그인_요청_실패_검증(response);
        };
    }

    private static void 로그인_요청_실패_검증(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static TokenResponse 로그인_요청_및_전체_검증(TokenRequest request) {
        ExtractableResponse<Response> response = 로그인_요청(request);

        로그인_요청_응답됨(response);

        TokenResponse tokenResponse = response.as(TokenResponse.class);
        로그인_요청_본문_검증(tokenResponse);

        return tokenResponse;
    }

    private static ExtractableResponse<Response> 로그인_요청(TokenRequest request) {
        return post(
                Given.builder().contentType(ContentType.JSON).body(request).build(),
                When.builder().uri("/login/token").build()
        );
    }

    private static void 로그인_요청_본문_검증(TokenResponse response) {
        assertThat(response.getAccessToken()).isNotBlank();
    }

    private static void 로그인_요청_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
