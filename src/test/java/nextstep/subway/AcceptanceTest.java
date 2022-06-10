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

    public static ExtractableResponse<Response> get(String path, Object... pathParams) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get(path, pathParams)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> post(Object requestBody, String path, Object... pathParams) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody)
            .when().post(path, pathParams)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> put(Object requestBody, String path, Object... pathParams) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody)
            .when().put(path, pathParams)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> delete(String path, Object... pathParams) {
        return RestAssured
            .given().log().all()
            .when().delete(path, pathParams)
            .then().log().all()
            .extract();
    }
}
