package nextstep.subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;

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

    public static ExtractableResponse<Response> sendPost(String path, Object request, Object... pathParams) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(path, pathParams)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> sendGet(String path, Object... pathParams) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(path, pathParams)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> sendPut(String path, Object request, Object... pathParams) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().put(path, pathParams)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> sendDelete(String path, Object... pathParams) {
        return RestAssured
                .given().log().all()
                .when().delete(path, pathParams)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> sendPostWithAuth(String accessToken, String path, Object request, Object... pathParams) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(path, pathParams)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> sendGetWithAuth(String accessToken, String path, Object... pathParams) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(path, pathParams)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> sendPutWithAuth(String accessToken, String path, Object request, Object... pathParams) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().put(path, pathParams)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> sendDeleteWithAuth(String accessToken, String path, Object... pathParams) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete(path, pathParams)
                .then().log().all()
                .extract();
    }
}
