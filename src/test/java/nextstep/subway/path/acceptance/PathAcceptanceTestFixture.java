package nextstep.subway.path.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.ListAssert;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class PathAcceptanceTestFixture {

    public static ExtractableResponse<Response> 지하철_최단경로_조회_요청(StationResponse upStation, StationResponse downStation) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/paths?source={upStationId}&target={downStationId}", upStation.getId(), downStation.getId())
            .then().log().all()
            .extract();
    }

    public static void 지하철_최단경로_조회_요청_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_최단경로_조회_요청_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_최단경로_조회_요청_포함됨(ExtractableResponse<Response> response, List<String> stations, int distance, int extraFare) {
        PathResponse pathResponse = response.as(PathResponse.class);

        assertAll(
            () -> assertThat(pathResponse.getStations()
                .stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList()))
                .hasSize(stations.size())
                .containsAll(stations),
            () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance),
            () -> assertThat(response.jsonPath().getInt("extraFare")).isEqualTo(extraFare)
        );
    }
}
