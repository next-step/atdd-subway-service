package nextstep.subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
    }

    public static ExtractableResponse<Response> get(String uri) {
        return RestAssured.given()
                .log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(uri)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> get(String uri, String accessToken) {
        return RestAssured.given()
                .log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "bearer " + accessToken)
                .when()
                .get(uri)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> post(String uri, Object params) {
        return RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .post(uri)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> post(String uri, Object params, String accessToken) {
        return RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "bearer " + accessToken)
                .body(params)
                .when()
                .post(uri)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> put(String uri, Object params) {
        return RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .put(uri)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> put(String uri, Object params, String accessToken) {
        return RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "bearer " + accessToken)
                .body(params)
                .when()
                .put(uri)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> delete(String uri) {
        return RestAssured.given()
                .log().all()
                .when()
                .delete(uri)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> delete(String uri, String accessToken) {
        return RestAssured.given()
                .log().all()
                .header(HttpHeaders.AUTHORIZATION, "bearer " + accessToken)
                .when()
                .delete(uri)
                .then()
                .log().all()
                .extract();
    }

}
