package nextstep.subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Objects;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

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

    public static ExtractableResponse<Response> post(String path, Object params, String token) {
        token = setBlankIfNull(token);
        return given()
                        .log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .auth().oauth2(token)
                .when()
                        .post(path)
                .then()
                        .log().all()
                        .extract();
    }

    public static ExtractableResponse<Response> post(String path, Object params) {
        return post(path, params, null);
    }

    public static ExtractableResponse<Response> put(String path, Object params, String token) {
        token = setBlankIfNull(token);
        return given()
                        .log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .auth().oauth2(token)
                .when()
                        .put(path)
                .then()
                        .log().all()
                        .extract();
    }

    public static ExtractableResponse<Response> put(String path, Object params) {
        return put(path, params, null);
    }

    public static ExtractableResponse<Response> get(String path, String token) {
        token = setBlankIfNull(token);
        return given()
                        .log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .auth().oauth2(token)
                .when()
                        .get(path)
                .then()
                        .log().all()
                        .extract();
    }

    public static ExtractableResponse<Response> get(String path) {
        return get(path, null);
    }

    public static ExtractableResponse<Response> delete(String path, String token) {
        token = setBlankIfNull(token);
        return given()
                        .log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .auth().oauth2(token)
                .when()
                        .delete(path)
                .then()
                        .log().all()
                        .extract();
    }

    public static ExtractableResponse<Response> delete(String path) {
        return delete(path, null);
    }

    public static void assertHttpStatus(ExtractableResponse<Response> response, HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

    private static String setBlankIfNull(String token) {
        if (Objects.isNull(token)) {
            token = "";
        }
        return token;
    }
}
