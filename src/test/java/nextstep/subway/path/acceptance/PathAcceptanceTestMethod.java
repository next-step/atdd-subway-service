package nextstep.subway.path.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class PathAcceptanceTestMethod {

    private PathAcceptanceTestMethod() {
    }

    public static ExtractableResponse<Response> 최단_경로_조회_요청(Long sourceStationId, Long targetStationId) {
        return RestAssured.given().log().all()
                .when()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .param("sourceId", sourceStationId)
                .param("targetId", targetStationId)
                .get("/paths")
                .then().log().all()
                .extract();
    }

    public static void 지하철_최단_경로_조회됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        PathResponse path = response.as(PathResponse.class);
        List<Long> stationIds = path.getStations().stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    public static void 최단경로_거리_조회됨(ExtractableResponse<Response> response, int distance) {
        PathResponse path = response.as(PathResponse.class);
        assertThat(path.getDistance()).isEqualTo(distance);
    }

    public static void 최단경로_요금_조회됨(ExtractableResponse<Response> response, int fare) {
        PathResponse path = response.as(PathResponse.class);
        assertThat(path.getDistance()).isEqualTo(fare);
    }

    public static void 지하철_최단_경로_조회_요청_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_최단경로_조회_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

}
