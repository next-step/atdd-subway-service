package nextstep.subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class RestAssuredApi {

    public static <T> ExtractableResponse<Response> post(String uri, T request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> get(String uri) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> get(String uri, Long id) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri, id)
                .then().log().all()
                .extract();
    }

    public static <T> ExtractableResponse<Response> put(String uri, T request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .put(uri).then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> delete(String uri) {
        return RestAssured
                .given().log().all()
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    public static <T> ExtractableResponse<Response> authPost(String uri, String token, T request) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> authGet(String uri, String token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all()
                .extract();
    }

    public static <T> ExtractableResponse<Response> authPut(String uri, String token, T request) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().put(uri)
                .then().log().all()
                .extract();
    }

    public static <T> ExtractableResponse<Response> authDelete(String uri, String token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .when().delete(uri)
                .then().log().all()
                .extract();
    }
}
