package nextstep.subway.line.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_목록_응답됨;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_목록_조회_요청;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_목록_포함되지않음;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_목록_포함됨;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_삭제됨;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_생성_요청;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_생성됨;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_수정_요청;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_수정됨;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_제거_요청;
import static nextstep.subway.station.step.StationAcceptanceStep.지하철역_등록되어_있음;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 광교역;
    private StationResponse 잠실역;
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;
    private LineRequest lineRequest3;

    /**
     * Background
     * Given 지하철역 등록되어 있음
     * And 지하철 노선 등록되어 있음
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);
        잠실역 = 지하철역_등록되어_있음("잠실역").as(StationResponse.class);

        lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        lineRequest2 = new LineRequest("구신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 15);
        lineRequest3 = new LineRequest("이호선", "bg-green-600", 강남역.getId(), 잠실역.getId(), 15);
    }

    /**
     * Scenario: 지하철 노선을 관리
     * When 지하철 노선 등록 요청
     * Then 지하철 노선 등록됨
     * When 지하철 노선 목록 조회 요청
     * then 지하철 노선 목록 응답됨
     * When 지하철 노선 수정 요청
     * Then 지하철 노선 수정됨
     * When 지하철 노선 삭제 요청
     **/
    @Test
    void 지하철_노선을_관리() {
        ExtractableResponse<Response> 노선_생성_결과1 = 지하철_노선_생성_요청(lineRequest1);
        ExtractableResponse<Response> 노선_생성_결과2 = 지하철_노선_생성_요청(lineRequest2);
        지하철_노선_생성됨(노선_생성_결과1);
        지하철_노선_생성됨(노선_생성_결과2);

        ExtractableResponse<Response> 목록_조회_결과 = 지하철_노선_목록_조회_요청();
        지하철_노선_목록_응답됨(목록_조회_결과);
        지하철_노선_목록_포함됨(목록_조회_결과, Arrays.asList(노선_생성_결과1, 노선_생성_결과2));

        ExtractableResponse<Response> 노선_수정_결과 = 지하철_노선_수정_요청(노선_생성_결과1, lineRequest3);
        지하철_노선_수정됨(노선_수정_결과);

        ExtractableResponse<Response> 노선_제거_결과 = 지하철_노선_제거_요청(노선_생성_결과1);
        지하철_노선_삭제됨(노선_제거_결과);

        ExtractableResponse<Response> 지하철_목록_조회_요청 = 지하철_노선_목록_조회_요청();
        지하철_노선_목록_응답됨(지하철_목록_조회_요청);
        지하철_노선_목록_포함되지않음(지하철_목록_조회_요청, Collections.singletonList(노선_생성_결과1));
    }
}
