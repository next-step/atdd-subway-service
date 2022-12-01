package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTestUtils {
    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        return RestAssured.given().log().all()
                .body(new TokenRequest(email, password))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그인되어_있음(String email, String password) {
        return 로그인_요청(email, password);
    }

    public static void 로그인_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 로그인_실패(ExtractableResponse<Response> response) {
        인증_실패(response);
    }

    public static void 인증_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
