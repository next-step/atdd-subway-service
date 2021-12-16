package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

public class RestAssuredUtil {

    public static ExtractableResponse<Response> jsonPost(final Object params, final String path) {
        return logAndExtractResponse(
            createAndLogTestSpecification()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(path)
        );
    }

    public static ExtractableResponse<Response> jsonPost(
        final Object params,
        final String path,
        final Object... pathParams
    ) {
        return logAndExtractResponse(
            createAndLogTestSpecification()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(path, pathParams)
        );
    }

    public static ExtractableResponse<Response> jsonGet(final String path) {
        return logAndExtractResponse(
            createAndLogTestSpecification()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(path)
        );
    }

    public static ExtractableResponse<Response> jsonGet(final String path, final Object... pathParams) {
        return logAndExtractResponse(
            createAndLogTestSpecification()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(path, pathParams)
        );
    }

    public static ExtractableResponse<Response> jsonGet(final Object params, final String path) {
        return logAndExtractResponse(
            createAndLogTestSpecification()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().get(path)
        );
    }

    public static ExtractableResponse<Response> jsonPut(final Object params, final String path) {
        return logAndExtractResponse(
            createAndLogTestSpecification()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put(path)
        );
    }

    public static ExtractableResponse<Response> delete(final String path) {
        return logAndExtractResponse(
            createAndLogTestSpecification()
                .when().delete(path)
        );
    }

    public static ExtractableResponse<Response> delete(
        final String path,
        final Object... pathParams
    ) {
        return logAndExtractResponse(
            createAndLogTestSpecification()
                .when().delete(path, pathParams)
        );
    }

    public static ExtractableResponse<Response> postWithAuth(
        final String token,
        final Object params,
        final String path
    ) {
        return logAndExtractResponse(
            createAndLogTestSpecification()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(path)
        );
    }

    public static ExtractableResponse<Response> getWithAuth(final String token, final String path) {
        return logAndExtractResponse(
            createAndLogTestSpecification()
                .auth().oauth2(token)
                .when().get(path)
        );
    }

    public static ExtractableResponse<Response> getWithAuth(
        final String token,
        final Object params,
        final String path
    ) {
        return logAndExtractResponse(
            createAndLogTestSpecification()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().get(path)
        );
    }

    public static ExtractableResponse<Response> deleteWithAuth(
        final String token,
        final String path
    ) {
        return logAndExtractResponse(
            createAndLogTestSpecification()
                .auth().oauth2(token)
                .when().delete(path)
        );
    }

    private static RequestSpecification createAndLogTestSpecification() {
        return RestAssured
            .given().log().all();
    }

    private static ExtractableResponse<Response> logAndExtractResponse(final Response response) {
        return response.then().log().all().extract();
    }
}
