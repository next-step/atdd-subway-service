package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.request.AcceptanceTestRequest;
import nextstep.subway.request.Given;
import nextstep.subway.request.When;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;

import static nextstep.subway.request.AcceptanceTestRequest.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceRequest {
    public static Executable 로그인_요청_및_검증(TokenRequest request, AuthToken authToken) {
        return () -> {
            TokenResponse tokenResponse = 로그인_요청_및_전체_검증(request);

            authToken.changeToken(tokenResponse.getAccessToken());
        };
    }
    
    public static Executable 로그인_요청_및_검증(TokenRequest request) {
        return () -> {
            로그인_요청_및_전체_검증(request);
        };
    }

    private static TokenResponse 로그인_요청_및_전체_검증(TokenRequest request) {
        ExtractableResponse<Response> response;
        response = post(
                Given.builder().contentType(ContentType.JSON).body(request).build(),
                When.builder().uri("/login/token").build()
        );

        로그인_요청_응답됨(response);

        TokenResponse tokenResponse = response.as(TokenResponse.class);
        로그인_요청_본문_검증(tokenResponse);

        return tokenResponse;
    }

    private static void 로그인_요청_본문_검증(TokenResponse response) {
        assertThat(response.getAccessToken()).isNotBlank();
    }

    private static void 로그인_요청_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
