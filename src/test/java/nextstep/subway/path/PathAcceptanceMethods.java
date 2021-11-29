package nextstep.subway.path;

import static nextstep.subway.AcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.StreamUtils;

public class PathAcceptanceMethods {
    private static final String PATH_URL_PATH = "/paths?";
    private static final String REQUEST_PARAM_SOURCE = "source=";
    private static final String REQUEST_PARAM_TARGET= "target=";
    private static final String AMPERSAND_SIGN = "&";

    private PathAcceptanceMethods() {}

    public static ExtractableResponse<Response> 지하철_최단경로_조회_요청(Long sourceId, Long targetId, TokenResponse token) {
        return getByAuth(PATH_URL_PATH + REQUEST_PARAM_SOURCE + sourceId + AMPERSAND_SIGN + REQUEST_PARAM_TARGET + targetId, token);
    }

    public static void 지하철_최단경로_조회됨(ExtractableResponse<Response> response,
                                          List<StationResponse> expectedStations,
                                          int expectedDistance,
                                          int expectedFare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        List<Long> actualIds = StreamUtils.mapToList(pathResponse.getStations(), StationResponse::getId);
        List<Long> expectedIds = StreamUtils.mapToList(expectedStations, StationResponse::getId);

        assertThat(actualIds).containsExactlyElementsOf(expectedIds);
        assertThat(pathResponse.getDistance()).isEqualTo(expectedDistance);
        assertThat(pathResponse.getFare()).isEqualTo(expectedFare);
    }

    public static void 지하철_최단경로_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
