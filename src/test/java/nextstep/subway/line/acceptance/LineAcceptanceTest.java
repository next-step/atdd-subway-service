package nextstep.subway.line.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.step.LineAcceptanceStep;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
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
        구신분당선 = new LineRequest("구신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 15);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = LineAcceptanceStep.지하철_노선_생성_요청(신분당선);

        // then
        LineAcceptanceStep.지하철_노선_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        LineAcceptanceStep.지하철_노선_등록되어_있음(신분당선);

        // when
        ExtractableResponse<Response> response = LineAcceptanceStep.지하철_노선_생성_요청(신분당선);

        // then
        LineAcceptanceStep.지하철_노선_생성_실패됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createResponse1 = LineAcceptanceStep.지하철_노선_등록되어_있음(신분당선);
        ExtractableResponse<Response> createResponse2 = LineAcceptanceStep.지하철_노선_등록되어_있음(구신분당선);

        // when
        ExtractableResponse<Response> response = LineAcceptanceStep.지하철_노선_목록_조회_요청();

        // then
        LineAcceptanceStep.지하철_노선_목록_응답됨(response);
        LineAcceptanceStep.지하철_노선_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse = LineAcceptanceStep.지하철_노선_등록되어_있음(신분당선);

        // when
        ExtractableResponse<Response> response = LineAcceptanceStep.지하철_노선_목록_조회_요청(createResponse);

        // then
        LineAcceptanceStep.지하철_노선_응답됨(response, createResponse);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        String name = "신분당선";
        ExtractableResponse<Response> createResponse = LineAcceptanceStep.지하철_노선_등록되어_있음(신분당선);

        // when
        ExtractableResponse<Response> response = LineAcceptanceStep.지하철_노선_수정_요청(createResponse, 구신분당선);

        // then
        LineAcceptanceStep.지하철_노선_수정됨(response);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = LineAcceptanceStep.지하철_노선_등록되어_있음(신분당선);

        // when
        ExtractableResponse<Response> response = LineAcceptanceStep.지하철_노선_제거_요청(createResponse);

        // then
        LineAcceptanceStep.지하철_노선_삭제됨(response);
    }
}
