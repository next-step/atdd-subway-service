package nextstep.subway.line.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_목록_조회_요청;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_조회_요청;
import static nextstep.subway.line.acceptance.step.LineSectionAcceptanceStep.등록한_지하철_구간이_반영된_역_목록이_조회됨;
import static nextstep.subway.line.acceptance.step.LineSectionAcceptanceStep.삭제한_지하철_구간이_반영된_역_목록이_조회됨;
import static nextstep.subway.line.acceptance.step.LineSectionAcceptanceStep.지하철_구간_삭제됨;
import static nextstep.subway.line.acceptance.step.LineSectionAcceptanceStep.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.line.acceptance.step.LineSectionAcceptanceStep.지하철_노선에_지하철역_등록됨;
import static nextstep.subway.line.acceptance.step.LineSectionAcceptanceStep.지하철_노선에_지하철역_제외_요청;

@DisplayName("지하철 구간 관련 기능")
public class LineSectionAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;

    /**
     * Background
     * Given 지하철역 등록되어 있음
     * And 지하철 노선 등록되어 있음
     * And 지하철 노선에 지하철역 등록되어 있음
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        신분당선 = 지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    /**
     * Scenario: 지하철 구간을 관리
     * When 지하철 구간 등록 요청
     * Then 지하철 구간 등록됨
     * When 지하철 노선에 등록된 역 목록 조회 요청
     * Then 등록한 지하철 구간이 반영된 역 목록이 조회됨
     * When 지하철 구간 삭제 요청
     * Then 지하철 구간 삭제됨
     * When 지하철 노선에 등록된 역 목록 조회 요청
     * Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨
     **/
    @Test
    void 지하철_구간을_관리() {
        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);
        ExtractableResponse<Response> 노선_조회_결과 = 지하철_노선_조회_요청(신분당선);
        지하철_노선에_지하철역_등록됨(노선_조회_결과);

        ExtractableResponse<Response> 지하철_노선_목록_조회_요청_결과 = 지하철_노선_목록_조회_요청(신분당선.getId());
        등록한_지하철_구간이_반영된_역_목록이_조회됨(지하철_노선_목록_조회_요청_결과, Arrays.asList(강남역, 양재역));

        ExtractableResponse<Response> 구간_삭제_결과 = 지하철_노선에_지하철역_제외_요청(신분당선, 양재역);
        지하철_구간_삭제됨(구간_삭제_결과);

        ExtractableResponse<Response> 지하철_노선_목록_조회_요청_결과2 = 지하철_노선_목록_조회_요청(신분당선.getId());
        삭제한_지하철_구간이_반영된_역_목록이_조회됨(지하철_노선_목록_조회_요청_결과2, Collections.singletonList(양재역));
    }
}
