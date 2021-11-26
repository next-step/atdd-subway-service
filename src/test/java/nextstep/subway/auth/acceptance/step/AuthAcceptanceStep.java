package nextstep.subway.auth.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.springframework.http.HttpStatus;

public class AuthAcceptanceStep {

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        return RestAssured.given().log().all()
            .body(new TokenRequest(email, password))
            .contentType(ContentType.JSON)
            .when()
            .post("/login/token")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 로그인_사용자_정보_요청(String accessToken) {
        return RestAssured.given().log().all()
            .auth().oauth2(accessToken)
            .when().get("/members/me")
            .then().log().all()
            .extract();
    }

    public static void 로그인_사용자_정보_조회_실패(ExtractableResponse<Response> response) {
        인증되지_않은_요청(response);
    }

    public static void 로그인_실패(ExtractableResponse<Response> response) {
        인증되지_않은_요청(response);
    }

    public static void 로그인_요청_실퍠(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).
            isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 로그인_됨(ExtractableResponse<Response> response) {
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.as(TokenResponse.class).getAccessToken()).isNotBlank()
        );
    }

    private static void 인증되지_않은_요청(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).
            isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
