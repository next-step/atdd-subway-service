package nextstep.subway.path.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class PathAcceptanceSteps {
    public static ExtractableResponse<Response> 지하철_최단_경로_조회(Long sourceId, Long targetId) {
        Map<String, String> params = new HashMap<>();
        params.put("source", sourceId + "");
        params.put("target", targetId + "");

        return RestAssured
            .given().log().all()
            .queryParams(params)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/paths")
            .then().log().all().extract();
    }

    public static void 지하철_최단_경로_지하철역_순서_정렬됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        PathResponse path = response.as(PathResponse.class);
        List<Long> stationIds = path.getStations().stream()
            .map(it -> it.getId())
            .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
            .map(it -> it.getId())
            .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    public static void 지하철_최단_경로_거리(ExtractableResponse<Response> response, int distance) {
        PathResponse path = response.as(PathResponse.class);

        assertThat(path.getDistance()).isEqualTo(distance);
    }

    public static void 지하철_최단_경로_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_최단_경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 지하철_요금_조회됨(ExtractableResponse<Response> response, int fare) {
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(fare);
    }
}
