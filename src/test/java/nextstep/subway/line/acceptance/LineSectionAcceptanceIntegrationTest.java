package nextstep.subway.line.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_목록_응답됨;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_조회_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_순서_정렬됨;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_제외_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_제외됨;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관련 기능 통합 테스트")
public class LineSectionAcceptanceIntegrationTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    /**
     * Feature: 지하철 구간 관련 기능
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선 등록되어 있음
     *     And 지하철 노선에 지하철역 등록되어 있음
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
    void totalTest() {
        지하철역_등록되어_있음();
        지하철_노선_등록되어_있음();
        지하철_노선에_지하철역_등록되어_있음();

        List<ExtractableResponse<Response>> 지하철_구간_등록_요청 = 지하철_구간_등록_요청();
        지하철_구간_등록됨(지하철_구간_등록_요청);

        ExtractableResponse<Response> 조회응답 = 지하철_노선에_등록된_역_목록_조회();
        등록한_지하철_구간이_반영된_역_목록이_조회됨(조회응답, Arrays.asList(강남역, 양재역, 정자역, 광교역));

        ExtractableResponse<Response> 삭제_요청 = 지하철_구간_삭제_요청();
        지하철_노선에_지하철역_제외됨(삭제_요청);

        조회응답 = 지하철_노선에_등록된_역_목록_조회();
        등록한_지하철_구간이_반영된_역_목록이_조회됨(조회응답, Arrays.asList(강남역, 정자역, 광교역));
    }

    private void 지하철역_등록되어_있음() {
        강남역 = 지하철역_등록되어_있음("강남역");
        양재역 = 지하철역_등록되어_있음("양재역");
        정자역 = 지하철역_등록되어_있음("정자역");
        광교역 = 지하철역_등록되어_있음("광교역");
    }

    public static StationResponse 지하철역_등록되어_있음(String 역이름) {
        return StationAcceptanceTest.지하철역_등록되어_있음(역이름).as(StationResponse.class);
    }

    private void 지하철_노선_등록되어_있음() {
        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    private void 지하철_노선에_지하철역_등록되어_있음() {
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선);
        지하철_노선_목록_응답됨(response);
        지하철_노선에_지하철역_등록됨(response);
    }

    private List<ExtractableResponse<Response>> 지하철_구간_등록_요청() {
        return Lists.newArrayList(
                지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3),
                지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 정자역, 3));
    }

    private void 지하철_구간_등록됨(List<ExtractableResponse<Response>> 등록응답) {
        등록응답.forEach(LineSectionAcceptanceTest::지하철_노선에_지하철역_등록됨);
    }

    private ExtractableResponse<Response> 지하철_노선에_등록된_역_목록_조회() {
        return 지하철_노선_조회_요청(신분당선);
    }

    private ExtractableResponse<Response> 지하철_구간_삭제_요청() {
        return 지하철_노선에_지하철역_제외_요청(신분당선, 양재역);
    }

    private void 등록한_지하철_구간이_반영된_역_목록이_조회됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        지하철_노선에_지하철역_순서_정렬됨(response, expectedStations);
    }

    private void 지하철_노선에_지하철역_등록됨(ExtractableResponse<Response> response) {
        LineResponse lineResponse = response.body().as(LineResponse.class);

        assertThat(lineResponse.getStations().stream().map(StationResponse::getName))
                .containsExactly(강남역.getName(), 광교역.getName());
    }

}
