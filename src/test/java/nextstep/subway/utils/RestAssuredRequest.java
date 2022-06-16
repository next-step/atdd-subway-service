package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.Map;

public class RestAssuredRequest {

    private RestAssuredRequest() {}

    public static ExtractableResponse<Response> getRequest(
            String path, Map<String, Object> params, Object... pathVariables
    ) {
        return RestAssured.given().log().all()
                .params(params)
                .when().get(path, pathVariables)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> postRequest(
            String path, Map<String, Object> params, Map<String, Object> body, Object... pathVariables
    ) {
        return RestAssured.given().log().all()
                .params(params)
                .body(body)
                .when().post(path, pathVariables)
                .then().log().all()
                .extract();
    }
}
