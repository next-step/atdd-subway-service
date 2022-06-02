package nextstep.subway.line.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.StreamUtils;
import org.springframework.http.HttpStatus;

public class LineSectionAcceptanceTestMethod extends AcceptanceTest {

    private static final String LINE_SECTION_PATH_WITH_LINE_ID_PATH_VALUE_FORMAT = "/lines/%s/sections";
    private static final String LINE_SECTION_PATH_WITH_LINE_ID_PATH_VALUE_AND_STATION_ID_PARAM_FORMAT = "/lines/%s/sections?stationId=%s";

    private LineSectionAcceptanceTestMethod() {}

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        SectionRequest sectionRequest = SectionRequest.of(upStation.getId(), downStation.getId(), distance);
        return post(String.format(LINE_SECTION_PATH_WITH_LINE_ID_PATH_VALUE_FORMAT, line.getId()), sectionRequest);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(LineResponse line, StationResponse station) {
        return delete(String.format(LINE_SECTION_PATH_WITH_LINE_ID_PATH_VALUE_AND_STATION_ID_PARAM_FORMAT, line.getId(), station.getId()));
    }


    public static void 지하철_노선에_지하철역_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선에_지하철역_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 지하철_노선에_지하철역_순서_정렬됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        LineResponse line = response.as(LineResponse.class);
        List<Long> stationIds = StreamUtils.mapToList(line.getStations(), StationResponse::getId);
        List<Long> expectedStationIds = StreamUtils.mapToList(expectedStations, StationResponse::getId);
        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    public static void 지하철_노선에_지하철역_제외됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선에_지하철역_제외_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
