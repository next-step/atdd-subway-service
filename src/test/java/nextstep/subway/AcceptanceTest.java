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

import java.util.HashMap;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    public static final String LINE_ROOT_PATH = "/lines";
    public static final String STATION_ROOT_PATH = "/stations";
    public static final String PATH_ROOT_PATH = "/paths";

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
    }

    public static <T> ExtractableResponse<Response> 생성_요청(String path, T requestBody) {
        return RestAssured
                .given().log().all()
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(path)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 조회_요청(String path) {
        return RestAssured
                .given().log().all()
                .when().get(path)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 조회_요청(String path, HashMap<String, ?> parametersMap) {
        return RestAssured
                .given().log().all()
                .queryParams(parametersMap)
                .when().get(path)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 조회_요청(String path, String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().get(path)
                .then().log().all().extract();
    }

    public static <T> ExtractableResponse<Response> 수정_요청(String path, T requestBody) {
        return RestAssured
                .given().log().all()
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(path)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 삭제_요청(String path) {
        return RestAssured
                .given().log().all()
                .when().delete(path)
                .then().log().all().extract();
    }
}
