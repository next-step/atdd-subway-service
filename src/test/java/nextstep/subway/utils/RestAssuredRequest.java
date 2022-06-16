package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.Map;

public class RestAssuredRequest {

    private RestAssuredRequest() {}

    public static ExtractableResponse<Response> getRequest(
            String path, Map<String, Object> params, String token, Object... pathVariables
    ) {
        return RestAssured.given().log().all()
                .params(params)
                .header("Bearer", token)
                .when().get(path, pathVariables)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> postRequest(
            String path, Map<String, Object> params, Object body, String token, Object... pathVariables
    ) {
        return RestAssured.given().log().all()
                .params(params)
                .header("Bearer", token)
                .body(body)
                .when().post(path, pathVariables)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> putRequest(
            String path, Map<String, Object> params, Object body, String token, Object... pathVariables
    ) {
        return RestAssured.given().log().all()
                .params(params)
                .header("Bearer", token)
                .body(body)
                .when().put(path, pathVariables)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteRequest(
            String path, Map<String, Object> params, String token, Object... pathVariables
    ) {
        return RestAssured.given().log().all()
                .params(params)
                .header("Bearer", token)
                .when().delete(path, pathVariables)
                .then().log().all()
                .extract();
    }
}
