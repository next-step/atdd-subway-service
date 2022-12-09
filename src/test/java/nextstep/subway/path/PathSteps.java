package nextstep.subway.path;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class PathSteps {

    public static ExtractableResponse<Response> 지하철_경로_조회_요청(String token, Long sourceId, Long targetId) {
        Map<String, String> params = new HashMap<>();
        params.put("source", String.valueOf(sourceId));
        params.put("target", String.valueOf(targetId));

        return given().auth().oauth2(token)
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/paths?source={source}&target={target}", sourceId, targetId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_경로_조회_요청(Long sourceId, Long targetId) {
        Map<String, String> params = new HashMap<>();
        params.put("source", String.valueOf(sourceId));
        params.put("target", String.valueOf(targetId));

        return given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/paths?source={source}&target={target}", sourceId, targetId)
                .then().log().all()
                .extract();
    }
}
