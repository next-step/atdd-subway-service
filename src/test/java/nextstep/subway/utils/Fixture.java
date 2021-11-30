package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class Fixture {

    public static ExtractableResponse<Response> get(String path) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getAuth(String path, TokenResponse token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> post(String path, Object param) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .when()
                .post(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> postAuth(String path, TokenResponse token, Object param) {
        return RestAssured.given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .when()
                .post(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> putAuth(String path, TokenResponse token, Object param) {
        return RestAssured.given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .when()
                .put(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteAuth(String path, TokenResponse token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteAuth(String path, TokenResponse token, Object... pathParams) {
        return RestAssured.given().log().all()
                .auth().oauth2(token.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(path, pathParams)
                .then().log().all()
                .extract();
    }

    public static void 응답_OK(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 응답_CREATE(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 응답_NO_CONTENT(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 응답_BAD_REQUEST(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 응답_SERVER_ERROR(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

}
