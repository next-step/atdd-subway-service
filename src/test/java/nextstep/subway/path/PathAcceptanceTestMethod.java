package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.StreamUtils;
import org.springframework.http.HttpStatus;

public class PathAcceptanceTestMethod extends AcceptanceTest {

    private static final String PATH_FINDER_PATH_FORMAT = "/paths?source=%s&target=%s";

    public static ExtractableResponse<Response> 지하철_최단경로_조회_요청(Long sourceId,
                                                               Long targetId,
                                                               TokenResponse tokenResponse) {
        return getWithAuth(String.format(PATH_FINDER_PATH_FORMAT, sourceId, targetId), tokenResponse);
    }

    public static void 지하철_최단경로_조회됨(ExtractableResponse<Response> response,
                                    List<StationResponse> expectedStations,
                                    int distance) {

        PathResponse pathResponse = response.as(PathResponse.class);
        List<Long> actualStationIds = StreamUtils.mapToList(pathResponse.getStations(),
                StationResponse::getId);

        List<Long> expectedStationIds = StreamUtils.mapToList(expectedStations, StationResponse::getId);

        assertAll(
                () -> assertThat(actualStationIds).containsExactlyElementsOf(expectedStationIds),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(distance)
        );
    }

    public static void 지하철_최단경로_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 지하철_최단경로_요금도_함께_조회됨(ExtractableResponse<Response> response,
                                           List<StationResponse> expectedStations,
                                           int distance,
                                           int fare) {
        지하철_최단경로_조회됨(response, expectedStations, distance);
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(fare);
    }
}
