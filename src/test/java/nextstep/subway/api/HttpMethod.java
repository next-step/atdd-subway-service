package nextstep.subway.api;

import org.springframework.http.*;

import io.restassured.*;
import io.restassured.response.*;

public class HttpMethod {
    public static ExtractableResponse<Response> get(String uri, Object... params) {
        return RestAssured.given().log().params()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get(uri, params)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> post(Object body, String uri, Object... params) {
        return RestAssured.given().log().params()
            .body(body)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post(uri, params)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> delete(String uri, Object... params) {
        return RestAssured.given().log().params()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .delete(uri, params)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> put(Object body, String uri, Object... params) {
        return RestAssured.given().log().params()
            .body(body)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .put(uri, params)
            .then().log().all()
            .extract();
    }
}
