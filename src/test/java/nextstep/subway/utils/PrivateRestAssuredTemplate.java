package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

//TODO
public final class PrivateRestAssuredTemplate {
    private final String token;
    private final String baseUrl;

    public PrivateRestAssuredTemplate(final String token, final String baseUrl) {
        this.token = token;
        this.baseUrl = baseUrl;
    }

    public ExtractableResponse<Response> get() {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(baseUrl)
                .then().log().all()
                .extract();
    }

    public <T> ExtractableResponse<Response> get(final Map<String, T> queryMap) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(queryMap)
                .when()
                .get(baseUrl)
                .then().log().all()
                .extract();
    }

    public <T> ExtractableResponse<Response> post(final T body) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when()
                .post(baseUrl)
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> delete() {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .when()
                .delete(baseUrl)
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> delete(Long id) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .when()
                .delete(String.format("%s/%s", baseUrl, id))
                .then().log().all()
                .extract();
    }

    public <T> ExtractableResponse<Response> put(final T body) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when()
                .put(baseUrl)
                .then().log().all()
                .extract();
    }


}
