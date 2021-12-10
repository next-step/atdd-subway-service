package nextstep.subway.utils;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class RequestUtil {
    private RequestUtil() {
    }

    public static ExtractableResponse<Response> get(final String path, final Object... pathParams) {
        return RestAssured
            .given().log().all()
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
}
