package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

public class RestAssuredUtils {

    private static RequestSpecification requestSpecification;

    static {
        requestSpecification = RestAssured.given()
            .log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE);
    }

    public static <T> ExtractableResponse<Response> post(
        final String urlTemplate,
        final T body
    ) {
        return requestSpecification.when()
            .body(body)
            .post(urlTemplate)
            .then().log().all()
            .extract();
    }

    public static <T> ExtractableResponse<Response> post(
        final String accessToken,
        final String urlTemplate,
        final T body
    ) {
        return requestSpecification.when()
            .body(body)
            .auth().oauth2(accessToken)
            .post(urlTemplate)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> get(final String urlTemplate) {
        return requestSpecification.when()
            .get(urlTemplate)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> get(
        final String accessToken,
        final String urlTemplate
    ) {
        return requestSpecification.when()
            .auth().oauth2(accessToken)
            .get(urlTemplate)
            .then().log().all()
            .extract();
    }

    public static <T> ExtractableResponse<Response> put(
        final String urlTemplate,
        final T body
    ) {
        return requestSpecification.when()
            .body(body)
            .put(urlTemplate)
            .then().log().all()
            .extract();
    }

    public static <T> ExtractableResponse<Response> put(
        final String accessToken,
        final String urlTemplate,
        final T body
    ) {
        return requestSpecification.when()
            .body(body)
            .auth().oauth2(accessToken)
            .put(urlTemplate)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> delete(
        final String urlTemplate,
        final Object... path
    ) {
        return requestSpecification.when()
            .delete(urlTemplate, path)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> delete(
        final String accessToken,
        final String urlTemplate,
        final Object... path
    ) {
        return requestSpecification.when()
            .auth().oauth2(accessToken)
            .delete(urlTemplate, path)
            .then().log().all()
            .extract();
    }
}
