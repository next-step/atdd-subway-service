package nextstep.subway.utils;

import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class RequestUtil {
    private RequestUtil() {
    }

    public static ExtractableResponse<Response> get(final String path) {
        return RestAssured
            .given().log().all()
            .when().get(path)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> get(
        final String path, final Map<String, Object> queryParams, final Object... pathParams
    ) {
        return RestAssured
            .given().log().all()
            .queryParams(queryParams)
            .when().get(path, pathParams)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> post(
        final String path, final Object bodyParam, final Object... pathParams
    ) {
        return RestAssured
            .given().log().all()
            .body(bodyParam).contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post(path, pathParams)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> put(
        final String path, final Object bodyParam, final Object... pathParams
    ) {
        return RestAssured
            .given().log().all()
            .body(bodyParam)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put(path, pathParams)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> delete(final String path, final Object... pathParams) {
        return RestAssured
            .given().log().all()
            .when().delete(path, pathParams)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> getWithAccessToken(final String path, final String accessToken) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .when().get(path)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> getWithAccessToken(
        final String path, final String accessToken, final Map<String, Object> queryParams, final Object... pathParams
    ) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .queryParams(queryParams)
            .when().get(path, pathParams)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> postWithAccessToken(
        final String path, final String accessToken, final Object bodyParam, final Object... pathParams
    ) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .body(bodyParam).contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post(path, pathParams)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> putWithAccessToken(
        final String path, final String accessToken, final Object bodyParam, final Object... pathParams
    ) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .body(bodyParam)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put(path, pathParams)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> deleteWithAccessToken(
        final String path, final String accessToken, final Object... pathParams
    ) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .when().delete(path, pathParams)
            .then().log().all().extract();
    }
}
