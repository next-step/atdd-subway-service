package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.MediaType;

public class PathFixture {

    public static ExtractableResponse<Response> 지하철_출발역에서_도착역_최단경로_조회(String accessToken,
        StationResponse source,
        StationResponse target) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(accessToken)
            .param("source", source.getId())
            .param("target", target.getId())
            .when().get("/paths")
            .then().log().all()
            .extract();
    }
}
