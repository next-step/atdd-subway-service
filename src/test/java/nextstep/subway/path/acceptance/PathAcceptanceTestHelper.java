package nextstep.subway.path.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.station.dto.StationResponse;

public class PathAcceptanceTestHelper {
    public static ExtractableResponse<Response> 최단_경로_조회_요청(Long sourceId, Long targetId) {
        return RestAssured
            .given().log().all()
            .queryParam("source", sourceId)
            .queryParam("target", targetId)
            .when().get("/paths")
            .then().log().all().extract();
    }

    public static void 최단_경로_목록_일치됨(ExtractableResponse<Response> response, Long... stationIds) {
        List<Long> returnIds = response.jsonPath()
            .getList("stations", StationResponse.class)
            .stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());
        assertThat(returnIds).containsExactly(stationIds);
    }

    public static void 최단_경로_거리_일치됨(ExtractableResponse<Response> response, int distance) {
        int resultDistance = response.jsonPath().getInt("distance");
        assertThat(resultDistance).isEqualTo(distance);
    }

    public static void 최단_경로_조회_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 최단_경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
