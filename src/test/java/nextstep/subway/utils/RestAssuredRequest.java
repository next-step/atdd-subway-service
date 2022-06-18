package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import org.springframework.http.MediaType;

import java.util.Map;

public class RestAssuredRequest {

    private RestAssuredRequest() {}

    public static ExtractableResponse<Response> getRequest(
            String path, Map<String, Object> params, String token, Object... pathVariables
    ) {
        return assuredWithAuth(token)
                .params(params)
                .when().get(path, pathVariables)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> postRequest(
            String path, Map<String, Object> params, Object body, String token, Object... pathVariables
    ) {
        return assuredWithAuth(token)
                .params(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post(path, pathVariables)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> putRequest(
            String path, Map<String, Object> params, Object body, String token, Object... pathVariables
    ) {
        return assuredWithAuth(token)
                .params(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().put(path, pathVariables)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteRequest(
            String path, Map<String, Object> params, String token, Object... pathVariables
    ) {
        return assuredWithAuth(token)
                .params(params)
                .when().delete(path, pathVariables)
                .then().log().all()
                .extract();
    }

    private static RequestSpecification assuredWithAuth(String token) {
        RequestSpecification spec = RestAssured.given().log().all();

        if (token != null) {
            spec.header(AuthorizationExtractor.AUTHORIZATION, AuthorizationExtractor.BEARER_TYPE + " " + token);
        }

        return spec;
    }
}
