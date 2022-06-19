package nextstep.subway.auth.factory;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceFactory {

    public static ExtractableResponse<Response> 로그인_요청(String name, String email) {
        TokenRequest tokenRequest = TokenRequest.of(name, email);

        return RestAssured
                .given().log().all()
                .body(tokenRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static String 로그인_되어_있음(String name, String email) {
        return 로그인_요청(name, email).as(TokenResponse.class).getAccessToken();
    }

    public static void 로그인_성공됨(ExtractableResponse<Response> response) {
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(TokenResponse.class).getAccessToken()).isNotNull()
        );
    }

    public static void 로그인_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
