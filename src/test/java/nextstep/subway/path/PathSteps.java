package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class PathSteps {

    public static ExtractableResponse<Response> 지하철_노선_최단경로_조회_요청(
            StationResponse source, StationResponse target) {
        return RestAssured.given().log().all()
                .queryParam("source", source.getId())
                .queryParam("target", target.getId())
                .when()
                .get("/paths")
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_최단경로_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_최단경로_목록_정렬됨(ExtractableResponse<Response> response,
                                          List<StationResponse> expected) {
        PathResponse pathResponse = response.as(PathResponse.class);
        List<Long> actualStationsId = pathResponse.getStations().stream()
                .map(Station::getId)
                .collect(Collectors.toList());

        List<Long> expectedId = expected.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(actualStationsId).containsExactlyElementsOf(expectedId);
    }

    public static void 지하철_노선_최단경로_거리_응답됨(ExtractableResponse<Response> response, int expectedDistance) {
        assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(expectedDistance);
    }

    public static void 지하철_노선_최단경로_이용_요금_응답됨(ExtractableResponse<Response> response, int expectedFare) {
        assertThat(response.as(PathResponse.class).getFare()).isEqualTo(expectedFare);
    }
}
