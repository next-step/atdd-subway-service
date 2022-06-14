package nextstep.subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
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

    public static ExtractableResponse<Response> get(String path, Object... pathParams) {
        return get(null, path, pathParams);
    }

    public static ExtractableResponse<Response> post(Object requestBody, String path, Object... pathParams) {
        return post(null, requestBody, path, pathParams);
    }

    public static ExtractableResponse<Response> put(Object requestBody, String path, Object... pathParams) {
        return put(null, requestBody, path, pathParams);
    }

    public static ExtractableResponse<Response> delete(String path, Object... pathParams) {
        return delete(null, path, pathParams);
    }

    public static ExtractableResponse<Response> get(String token, String path, Object... pathParams) {
        return given(token)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get(path, pathParams)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> post(String token, Object requestBody, String path, Object... pathParams) {
        return given(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody)
            .when().post(path, pathParams)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> put(String token, Object requestBody, String path, Object... pathParams) {
        return given(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody)
            .when().put(path, pathParams)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> delete(String token, String path, Object... pathParams) {
        return given(token)
            .when().delete(path, pathParams)
            .then().log().all()
            .extract();
    }

    private static RequestSpecification given(String token) {
        RequestSpecification given = RestAssured.given().log().all();
        if (token != null) {
            given.auth().oauth2(token);
        }

        return given;
    }

}
