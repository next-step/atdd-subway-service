package nextstep.subway.path.step;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathStationResponse;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.groups.Tuple;
import org.springframework.http.HttpStatus;


public final class PathAcceptanceStep {

    private PathAcceptanceStep() {
        throw new AssertionError();
    }

    public static ExtractableResponse<Response> 지하철_최단_경로_조회(Long sourceId, Long targetId) {
        return RestAssured.given().log().all()
            .param("source", sourceId)
            .param("target", targetId)
            .when()
            .get("/paths")
            .then().log().all()
            .extract();
    }

    public static void 지하철_최단_경로_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
            .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_역_최단_경로_포함됨(ExtractableResponse<Response> response,
        List<StationResponse> expectedStations, int expectedDistance) {
        PathResponse path = response.as(PathResponse.class);
        assertAll(
            () -> assertThat(path.getDistance()).isEqualTo(expectedDistance),
            () -> assertThat(path.getStations())
                .doesNotHaveDuplicates()
                .extracting(
                    PathStationResponse::getId,
                    PathStationResponse::getName,
                    PathStationResponse::getCreatedDate)
                .containsExactly(expectedStations.stream()
                    .map(station -> Tuple.tuple(
                        station.getId(),
                        station.getName(),
                        station.getCreatedDate()))
                    .toArray(Tuple[]::new))
        );
    }

    public static void 지하철_최단_경로_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
            .isEqualTo(HttpStatus.OK.value());
    }
}
