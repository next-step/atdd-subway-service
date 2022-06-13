package nextstep.subway.line.acceptance;

import static nextstep.subway.behaviors.SubwayBehaviors.지하철_노선_목록_조회_요청;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철_노선_목록_포함됨;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철_노선_삭제_실패됨;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철_노선_삭제됨;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철_노선_생성_실패됨;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철_노선_생성_요청;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철_노선_생성됨;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철_노선_수정_실패됨;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철_노선_수정_요청;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철_노선_수정됨;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철_노선_응답됨;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철_노선_제거_요청;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철_노선_조회_요청;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철역_등록되어_있음;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 광교역;
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;
    private LineRequest lineRequest3;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);

        lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        lineRequest2 = new LineRequest("구신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 15);
        lineRequest3 = new LineRequest();
        ReflectionTestUtils.setField(lineRequest3, "name", "신구분당선");
        ReflectionTestUtils.setField(lineRequest3, "color", "bg-red-700");
    }

    /**
     * Feature: 지하철 노선 관련 기능
     * <p>
     * Background Given 지하철역 등록되어 있음
     * <p>
     * Scenario: 지하철 노선 관리 성공 When 지하철 노선 생성 요청(신분당선) Then 지하철 노선 생성됨
     * <p>
     * When 지하철 노선 조회 요청 Then 지하철_노선_응답됨
     * <p>
     * When 지하철 노선 생성 요청(구신분당선) Then 지하철 노선 생성됨
     * <p>
     * When 지하철 노선 목록 조회 요청 Then 지하철_노선_목록 응답됨
     * <p>
     * When 지하철 노선 수정 요청(구신분당선 -> 신구분당선) Then 지하철_노선_수정됨
     * <p>
     * When 지하철 노선 제거 요청(신구분당선) Then 지하철 노선 삭제됨
     * <p>
     * When 지하철 노선 목록 조회 요청 Then 지하철_노선_목록 응답됨
     */
    @DisplayName("지하철 노선관리 성공시나리오")
    @Test
    void lineManageSuccessScenario() {
        ExtractableResponse<Response> createResponse1 = 지하철_노선_생성_요청(lineRequest1);
        지하철_노선_생성됨(createResponse1);

        ExtractableResponse<Response> response = 지하철_노선_조회_요청(createResponse1.as(LineResponse.class));
        지하철_노선_응답됨(response);

        ExtractableResponse<Response> createResponse2 = 지하철_노선_생성_요청(lineRequest2);
        지하철_노선_생성됨(createResponse2);

        response = 지하철_노선_목록_조회_요청();
        지하철_노선_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));

        지하철_노선_수정_요청(createResponse2, lineRequest3);
        지하철_노선_수정됨(response);

        response = 지하철_노선_제거_요청(createResponse2);
        지하철_노선_삭제됨(response);

        response = 지하철_노선_목록_조회_요청();
        지하철_노선_목록_포함됨(response, Arrays.asList(createResponse1));
    }

    /**
     * Feature: 지하철 노선 관련 기능
     * <p>
     * Background Given 지하철역 등록되어 있음
     * <p>
     * Scenario: 지하철 노선 관리 실패 When 지하철 노선 생성 요청(신분당선) Then 지하철 노선 생성됨
     * <p>
     * When 지하철 노선 생성 요청(신분당선) Then 지하철 노선 생성 실패_중복생성 불가능
     * <p>
     * When 지하철 노선 제거 요청(신분당선) Then 지하철 노선 제거 성공
     * <p>
     * When 지하철 노선 제거 요청(신분당선) Then 지하철 노선 제거 실패_존재하지않는_노선
     * <p>
     * When 지하철 노선 수정 요청(신분당선 -> 신구분당선) Then 지하철_노선_수정_실패_존재하지않는_노선
     */
    @DisplayName("지하철 노선관리 실패시나리오")
    @Test
    void lineManageFailureScenario() {
        ExtractableResponse<Response> createResponse1 = 지하철_노선_생성_요청(lineRequest1);
        지하철_노선_생성됨(createResponse1);

        ExtractableResponse<Response> createFailResponse1 = 지하철_노선_생성_요청(lineRequest1);
        지하철_노선_생성_실패됨(createFailResponse1);

        ExtractableResponse<Response> deleteResponse = 지하철_노선_제거_요청(createResponse1);
        지하철_노선_삭제됨(deleteResponse);

        deleteResponse = 지하철_노선_제거_요청(createResponse1);
        지하철_노선_삭제_실패됨(deleteResponse);

        ExtractableResponse<Response> updateResponse = 지하철_노선_수정_요청(createResponse1, lineRequest2);
        지하철_노선_수정_실패됨(updateResponse);
    }
}
