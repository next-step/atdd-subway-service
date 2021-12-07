package nextstep.subway.path.acceptance.step;

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

public class PathAcceptanceStep {

    public static void 경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 경로_조회(Long source, Long target) {
        Map<String, Long> query = new HashMap<>();
        query.put("source", source);
        query.put("target", target);
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParams(query)
            .when().get("/paths")
            .then().log().all().
            extract();
    }

    public static void 최단경로_조회_됨(ExtractableResponse<Response> response, List<Long> expected) {
        PathResponse line = response.as(PathResponse.class);
        List<Long> stationIds = line.getStations().stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expected);
    }

    public static void 최단경로_조회_길이_계산됨(ExtractableResponse<Response> response, int expected) {
        PathResponse line = response.as(PathResponse.class);
        assertThat(line).extracting("distance").isEqualTo(expected);
    }
}
