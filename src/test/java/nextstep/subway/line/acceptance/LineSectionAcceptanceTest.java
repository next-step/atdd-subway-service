package nextstep.subway.line.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.RequestUtil;
import nextstep.subway.utils.StatusCodeCheckUtil;

@DisplayName("지하철 구간 관련 기능")
public class LineSectionAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 광교역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    @Test
    void 지하철_구간을_관리() {
        // when
        final ExtractableResponse<Response> createResponse = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);

        // then
        지하철_노선에_지하철역_등록됨(createResponse);

        // when
        final ExtractableResponse<Response> searchResponse1 = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);

        // then
        지하철_노선에_지하철역_순서_정렬된_상태로_조회됨(searchResponse1, Arrays.asList(강남역, 양재역, 광교역));

        // when
        final ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 광교역);

        // then
        지하철_노선에_지하철역_제외됨(removeResponse);

        // when
        final ExtractableResponse<Response> searchResponse2 = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);

        // then
        지하철_노선에_지하철역_순서_정렬된_상태로_조회됨(searchResponse2, Arrays.asList(강남역, 양재역));
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(
        LineResponse line, StationResponse upStation, StationResponse downStation, int distance
    ) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);
        return RequestUtil.post("/lines/{lineId}/sections", sectionRequest, line.getId());
    }

    public static void 지하철_노선에_지하철역_등록됨(ExtractableResponse<Response> response) {
        StatusCodeCheckUtil.ok(response);
    }

    public static void 지하철_노선에_지하철역_순서_정렬된_상태로_조회됨(
        ExtractableResponse<Response> response, List<StationResponse> expectedStations
    ) {
        LineResponse line = response.as(LineResponse.class);
        List<Long> stationIds = line.getStations().stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(LineResponse line, StationResponse station) {
        return RequestUtil.delete("/lines/{lineId}/sections?stationId={stationId}", line.getId(), station.getId());
    }

    public static void 지하철_노선에_지하철역_제외됨(ExtractableResponse<Response> response) {
        StatusCodeCheckUtil.ok(response);
    }
}
