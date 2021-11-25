package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

public class RestAssuredTestUtils {

    public static ExtractableResponse<Response> get(String uri, Object... params) {
        return given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri, params)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> post(String uri, Object body, Object... params) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post(uri, params)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> put(String uri, Object body, Object... params) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().put(uri, params)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> delete(String uri, Object... params) {
        return given()
                .when().delete(uri, params)
                .then().log().all()
                .extract();
    }

    private static RequestSpecification given() {
        return RestAssured
                .given().log().all();
    }
}
