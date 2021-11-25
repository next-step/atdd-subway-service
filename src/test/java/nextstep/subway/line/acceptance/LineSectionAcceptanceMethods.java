package nextstep.subway.line.acceptance;

import static nextstep.subway.AcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.StreamUtils;

public class LineSectionAcceptanceMethods {
    private static final String LINE_URL_PATH = "/lines";
    private static final String SECTION_URL_PATH = "/sections";
    private static final String WITH_STATION_ID_QUERY_PARAM = "?stationId=";
    private static final String SLASH_SIGN = "/";

    private LineSectionAcceptanceMethods() {}

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(Long lineId, SectionRequest sectionRequest) {
        return post(LINE_URL_PATH + SLASH_SIGN + lineId + SECTION_URL_PATH, sectionRequest);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(Long lindId, Long stationId) {
        return delete(LINE_URL_PATH + SLASH_SIGN + lindId + SECTION_URL_PATH + WITH_STATION_ID_QUERY_PARAM + stationId);
    }

    public static void 지하철_노선에_지하철역_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선에_지하철역_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 지하철_노선에_지하철역_제외됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선에_지하철역_제외_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 지하철_노선에_지하철역_순서_정렬됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        LineResponse line = response.as(LineResponse.class);
        List<Long> stationIds = StreamUtils.mapToList(line.getStations(), StationResponse::getId);
        List<Long> expectedStationIds = StreamUtils.mapToList(expectedStations, StationResponse::getId);

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }
}
