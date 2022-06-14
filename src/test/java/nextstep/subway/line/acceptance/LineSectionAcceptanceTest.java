package nextstep.subway.line.acceptance;

import static nextstep.subway.behaviors.SubwayBehaviors.지하철_노선_등록되어_있음;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철_노선_조회_요청;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철_노선에_지하철역_등록_실패됨;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철_노선에_지하철역_등록됨;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철_노선에_지하철역_순서_정렬됨;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철_노선에_지하철역_제외_실패됨;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철_노선에_지하철역_제외_요청;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철_노선에_지하철역_제외됨;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철역_등록되어_있음;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 구간 관련 기능")
class LineSectionAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);
        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 광교역, 10,0);

    }

    /**
     * Feature: 지하철 구간 관련 기능
     * <p>
     * Background Given 지하철역 등록되어 있음 And 지하철 노선 등록되어 있음 And 지하철 노선에 지하철역 등록되어 있음
     * <p>
     * Scenario: 지하철 구간 관리 성공 When 지하철 구간 등록 요청 Then 지하철 구간 등록됨 When 지하철 노선에 등록된 역 목록 조회 요청 Then 등록한 지하철 구간이 반영된 역 목록이
     * 조회됨 When 지하철 구간 삭제 요청 Then 지하철 구간 삭제됨 When 지하철 노선에 등록된 역 목록 조회 요청 Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨
     */
    @DisplayName("지하철 구간 관리 성공 시나리오")
    @Test
    void sectionManageSuccessScenario() {
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);
        지하철_노선에_지하철역_등록됨(response);

        response = 지하철_노선_조회_요청(신분당선);
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(강남역, 양재역, 광교역));

        response = 지하철_노선에_지하철역_제외_요청(신분당선, 양재역);
        지하철_노선에_지하철역_제외됨(response);

        response = 지하철_노선_조회_요청(신분당선);
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(강남역, 광교역));
    }

    /**
     * Feature: 지하철 구간 관련 기능
     * <p>
     * Background Given 지하철역 등록되어 있음 And 지하철 노선 등록되어 있음 And 지하철 노선에 지하철역 등록되어 있음
     * <p>
     * Scenario: 지하철 구간 관리 실패 When 지하철 구간 등록 요청_이미 등록된 구간 Then 지하철 구간 등록 불가 When 지하철 구간 등록 요청_기준위치없음 Then 지하철 구간 등록 불가
     * When 지하철 구간 등록 요청_적절하지않은 구간의 거리 Then 지하철 구간 등록 불가 When 지하철 구간 삭제 요청_최소 구간 수 유지 불가능한 경우 Then 지하철 구간 삭제 실패
     */
    @DisplayName("지하철 구간 관리 실패 시나리오")
    @Test
    void sectionManageFailureScenario() {
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 광교역, 3);
        지하철_노선에_지하철역_등록_실패됨(response);

        response = 지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 정자역, 3);
        지하철_노선에_지하철역_등록_실패됨(response);

        response = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 10);
        지하철_노선에_지하철역_등록_실패됨(response);

        response = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);
        지하철_노선에_지하철역_제외_실패됨(response);
    }
}
