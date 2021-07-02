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

import static nextstep.subway.line.acceptance.LineSectionAcceptanceStep.*;

@DisplayName("지하철 구간 관련 기능")
class LineSectionAcceptanceTest extends AcceptanceTest {
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

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10, 800);
        신분당선 = LineAcceptanceStep.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    @DisplayName("지하철 구간을 관리")
    @Test
    void integrationSections() {
        // When 지하철 구간 등록 요청
        ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청_결과 = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 1);

        // Then 지하철 구간 등록됨
        지하철_노선에_지하철역_등록됨(지하철_노선에_지하철역_등록_요청_결과);

        // When 지하철 노선에 등록된 역 목록 조회 요청
        ExtractableResponse<Response> 지하철_노선_조회_요청_결과 = LineAcceptanceStep.지하철_노선_조회_요청(신분당선);

        // Then 등록한 지하철 구간이 반영된 역 목록이 조회됨
        지하철_노선에_지하철역_순서_정렬됨(지하철_노선_조회_요청_결과, Arrays.asList(강남역, 양재역, 광교역));

        // When 지하철 구간 삭제 요청
        ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청_결과 = 지하철_노선에_지하철역_제외_요청(신분당선, 양재역);

        // Then 지하철 구간 삭제됨
        지하철_노선에_지하철역_제외됨(지하철_노선에_지하철역_제외_요청_결과);

        // When 지하철 노선에 등록된 역 목록 조회 요청
        ExtractableResponse<Response> 지하철_노선_조회_요청_제외_결과 = LineAcceptanceStep.지하철_노선_조회_요청(신분당선);

        // Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨
        지하철_노선에_지하철역_순서_정렬됨(지하철_노선_조회_요청_제외_결과, Arrays.asList(강남역, 광교역));
    }
}
