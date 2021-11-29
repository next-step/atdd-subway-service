package nextstep.subway.path.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathStep {

    public static void 최단_경로_조회됨(ExtractableResponse<Response> response, int distance, List<StationResponse> stationResponses) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(PathResponse.class).getStations()).containsAll(stationResponses);
        assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(distance);
    }

    public static ExtractableResponse<Response> 최단_경로_조회_요청(StationResponse source, StationResponse target) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("source", source.getId())
                .param("target", target.getId())
                .when().get("/path")
                .then().log().all()
                .extract();
    }
}
