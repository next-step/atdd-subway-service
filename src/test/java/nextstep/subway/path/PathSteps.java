package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.MediaType;

public class PathSteps {

    public static ExtractableResponse<Response> 최단_경로_조회_요청(StationResponse sourceStation, StationResponse targetStation) {
        return 최단_경로_조회_요청(sourceStation.getId(), targetStation.getId());
    }

    public static ExtractableResponse<Response> 최단_경로_조회_요청(Long sourceStationId, Long targetStationId) {
        return RestAssured
                .given().log().all()
                .param("source", sourceStationId)
                .param("target", targetStationId)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }
}
