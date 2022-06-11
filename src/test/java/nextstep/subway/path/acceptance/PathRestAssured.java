package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class PathRestAssured {
    public static ExtractableResponse<Response> 지하철_경로_최단거리_요청(long source, long target) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .param("source", source)
                .param("target", target)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }
}
