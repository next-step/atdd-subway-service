package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import org.springframework.http.MediaType;

public class RestAssuredRequest {

    public static ExtractableResponse<Response> post(String path, Object request) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post(path)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> postWithOAuth(String path, Object request, String token) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post(path)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> get(String path) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get(path)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> getWithOAuth(String path, String token) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get(path)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> put(String path, Object request) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().put(path)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> putWithOAuth(String path, Object request, String token) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().put(path)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> delete(String path) {
        return RestAssured.given().log().all()
            .when()
            .delete(path)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> deleteWithOAuth(String path, String token) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .when().delete(path)
            .then().log().all()
            .extract();
    }
}
