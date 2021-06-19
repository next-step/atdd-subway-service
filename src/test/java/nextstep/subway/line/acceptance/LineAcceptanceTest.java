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

import static nextstep.subway.line.acceptance.LineAcceptanceStep.*;

@DisplayName("지하철 노선 관련 기능")
class LineAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 광교역;
    private LineRequest 신분당선;
    private LineRequest 구신분당선;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        신분당선 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        구신분당선 = new LineRequest("구신분당선", "bg-red-500", 강남역.getId(), 광교역.getId(), 15);
    }

    @DisplayName("지하철 노선을 관리")
    @Test
    void integrationLines() {
        // When 지하철 노선 등록 요청
        ExtractableResponse<Response> 지하철_노선_생성_요청_결과 = 지하철_노선_생성_요청(신분당선);

        // Then 지하철 노선 등록됨
        지하철_노선_생성됨(지하철_노선_생성_요청_결과);

        // When 지하철 노선 목록 조회 요청
        ExtractableResponse<Response> 지하철_노선_목록_조회_요청_결과 = 지하철_노선_목록_조회_요청();

        // Then 등록한 지하철 노선 목록이 추가로 조회됨
        지하철_노선_목록_응답됨(지하철_노선_목록_조회_요청_결과);
        지하철_노선_목록_포함됨(지하철_노선_목록_조회_요청_결과, Arrays.asList(지하철_노선_생성_요청_결과));

        // When 특정한 지하철 노선 조회 요청
        ExtractableResponse<Response> 지하철_노선_조회_요청_결과 = 지하철_노선_조회_요청(지하철_노선_생성_요청_결과.as(LineResponse.class));

        // Then 요청한 지하철 노선이 조회됨
        지하철_노선_응답됨(지하철_노선_조회_요청_결과);
        지하철_노선_포함됨(지하철_노선_조회_요청_결과, 지하철_노선_생성_요청_결과);

        // When 지하철 노선 수정 요청
        ExtractableResponse<Response> 지하철_노선_수정_요청_결과 = 지하철_노선_수정_요청(지하철_노선_생성_요청_결과, 구신분당선);

        // Then 지하철 노선 수정됨
        지하철_노선_수정됨(지하철_노선_수정_요청_결과);

        // When 수정한 지하철 노선 조회 요청
        ExtractableResponse<Response> 지하철_노선_조회_요청_수정_결과 = 지하철_노선_조회_요청(지하철_노선_생성_요청_결과.as(LineResponse.class));

        // Then 수정된 지하철 노선이 조회됨
        지하철_노선_응답됨(지하철_노선_조회_요청_수정_결과);
        지하철_노선_포함됨(지하철_노선_조회_요청_수정_결과, 구신분당선);

        // When 지하철 노선 제거 요청
        ExtractableResponse<Response> 지하철_노선_제거_요청_결과 = 지하철_노선_제거_요청(지하철_노선_생성_요청_결과);

        // Then 지하철 노선 제거됨
        지하철_노선_삭제됨(지하철_노선_제거_요청_결과);

        // When 지하철 노선 목록 조회 요청
        ExtractableResponse<Response> 지하철_노선_제거_후_목록_조회_요청_결과 = 지하철_노선_목록_조회_요청();

        // Then 지하철 노선 목록에 제거되어 조회됨
        지하철_노선_목록_포함됨(지하철_노선_제거_후_목록_조회_요청_결과, Collections.emptyList());
    }
}
