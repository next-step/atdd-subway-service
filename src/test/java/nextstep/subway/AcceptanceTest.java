package nextstep.subway;

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

    protected static ExtractableResponse<Response> get(
        String path,
        Map<String, ?> queryParams
    ) {
        return RestAssured
            .given().log().all()
            .queryParams(queryParams)
            .when().get(path)
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
