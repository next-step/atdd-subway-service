package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class PathHelper {

    public static ExtractableResponse<Response> 최단_경로(Long source, Long target) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams("source", source, "target", target)
                .when().get("/paths")
                .then().log().all().extract();
    }
}
