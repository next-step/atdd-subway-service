package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class PathAcceptanceMethod {
    public static ExtractableResponse<Response> 지하철_최단경로_조회_요청(Long sourceId, Long targetId) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("source", sourceId)
                .param("target", targetId)
                .when().get("/paths")
                .then().log().all().
                extract();
    }

    public static void 지하철_최단경로_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_최단경로_포함됨(ExtractableResponse<Response> response, List<StationResponse> stationResponses) {
        List<Long> expectedStationIds = getStationIds(stationResponses);
        List<Long> resultStationIds = getStationIds(response.jsonPath().getList(".", StationResponse.class));

        assertThat(resultStationIds).containsExactlyElementsOf(expectedStationIds);
    }

    public static void 지하철_최단경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static List<Long> getStationIds(List<StationResponse> stationResponses) {
        return stationResponses.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
    }
}
