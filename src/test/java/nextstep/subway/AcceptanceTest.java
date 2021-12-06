package nextstep.subway;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.DatabaseCleanup;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
    }

    protected static <T> ExtractableResponse<Response> post(String path, T body) {
        return post(path, new HashMap<>(), body);
    }

    protected static <T> ExtractableResponse<Response> post(
        String path,
        Map<String, ?> pathParams,
        T body
    ) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().post(path, pathParams)
            .then().log().all()
            .extract();
    }

    protected static ExtractableResponse<Response> get(String path) {
        return get(path, new HashMap<>(), new HashMap<>());
    }

    protected static ExtractableResponse<Response> get(
        String path,
        String accessToken
    ) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get(path)
            .then().log().all()
            .extract();
    }

    protected static ExtractableResponse<Response> get(
        String path,
        Map<String, ?> pathParams,
        Map<String, ?> queryParams
    ) {
        return RestAssured
            .given().log().all()
            .queryParams(queryParams)
            .when().get(path, pathParams)
            .then().log().all()
            .extract();
    }

    protected static <T> ExtractableResponse<Response> put(
        String path,
        T body
    ) {
        return put(path, new HashMap<>(), body);
    }

    protected static <T> ExtractableResponse<Response> put(
        String path,
        String accessToken,
        T body
    ) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().put(path)
            .then().log().all()
            .extract();
    }

    protected static <T> ExtractableResponse<Response> put(
        String path,
        Map<String, ?> pathParams,
        T body
    ) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().put(path, pathParams)
            .then().log().all()
            .extract();
    }

    protected static ExtractableResponse<Response> delete(String path) {
        return delete(path, new HashMap<>(), new HashMap<>());
    }

    protected static ExtractableResponse<Response> delete(
        String path,
        String accessToken
    ) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().delete(path)
            .then().log().all()
            .extract();
    }

    protected static ExtractableResponse<Response> delete(
        String path,
        Map<String, ?> pathParams,
        Map<String, ?> queryParams
    ) {
        return RestAssured
            .given().log().all()
            .queryParams(queryParams)
            .when().delete(path, pathParams)
            .then().log().all()
            .extract();
    }
}
