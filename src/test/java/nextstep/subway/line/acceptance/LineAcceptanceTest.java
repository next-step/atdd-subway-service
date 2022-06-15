package nextstep.subway.line.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.ui.LineControllerTest;
import nextstep.subway.line.ui.SectionControllerTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 노선 시나리오 기반 인수테스트")
public class LineAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 광교역;

    private LineResponse 신분당선;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);
        신분당선 = LineControllerTest.지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10)).as(LineResponse.class);
    }

    /**
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선 등록되어 있음
     *     And 지하철 노선에 지하철역 등록되어 있음
     *
     *   Scenario: 지하철 구간을 관리
     *     When 지하철 구간 등록 요청
     *     Then 지하철 구간 등록됨
     *     When 지하철 노선에 등록된 역 목록 조회 요청
     *     Then 등록한 지하철 구간이 반영된 역 목록이 조회됨
     *     When 지하철 구간 삭제 요청
     *     Then 지하철 구간 삭제됨
     *     When 지하철 노선에 등록된 역 목록 조회 요청
     *     Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨
     */
    @Test
    @DisplayName("지하철 구간을 관리")
    void scenario() {
        // when
        final ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청 = SectionControllerTest.지하철_노선에_지하철역_등록_요청(신분당선, 강남역,
                양재역, 2);

        // then
        SectionControllerTest.지하철_노선에_지하철역_등록됨(지하철_노선에_지하철역_등록_요청);

        // when
        final ExtractableResponse<Response> 지하철_노선_조회_요청 = LineControllerTest.지하철_노선_조회_요청(신분당선);

        // then
        SectionControllerTest.지하철_노선에_지하철역_순서_정렬됨(지하철_노선_조회_요청, Arrays.asList(강남역, 양재역, 광교역));

        // when
        final ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청 = SectionControllerTest.지하철_노선에_지하철역_제외_요청(신분당선, 양재역);

        // then
        SectionControllerTest.지하철_노선에_지하철역_제외됨(지하철_노선에_지하철역_제외_요청);

        // when
        final ExtractableResponse<Response> 삭제_후_지하철_노선_조회_요청 = LineControllerTest.지하철_노선_조회_요청(신분당선);

        // then
        SectionControllerTest.지하철_노선에_지하철역_순서_정렬됨(삭제_후_지하철_노선_조회_요청, Arrays.asList(강남역, 광교역));
    }
}
