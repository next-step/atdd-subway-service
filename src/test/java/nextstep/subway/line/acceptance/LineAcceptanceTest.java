package nextstep.subway.line.acceptance;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.StationAcceptanceTestHelper;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 광교역;
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = StationAcceptanceTestHelper.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = StationAcceptanceTestHelper.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        lineRequest2 = new LineRequest("구신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 15);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = LineAcceptanceTestHelper.지하철_노선_생성_요청(lineRequest1);

        // then
        LineAcceptanceTestHelper.지하철_노선_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        LineAcceptanceTestHelper.지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = LineAcceptanceTestHelper.지하철_노선_생성_요청(lineRequest1);

        // then
        LineAcceptanceTestHelper.지하철_노선_생성_실패됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createResponse1 = LineAcceptanceTestHelper.지하철_노선_등록되어_있음(lineRequest1);
        ExtractableResponse<Response> createResponse2 = LineAcceptanceTestHelper.지하철_노선_등록되어_있음(lineRequest2);

        // when
        ExtractableResponse<Response> response = LineAcceptanceTestHelper.지하철_노선_목록_조회_요청();

        // then
        LineAcceptanceTestHelper.지하철_노선_목록_응답됨(response);
        LineAcceptanceTestHelper.지하철_노선_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse = LineAcceptanceTestHelper.지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = LineAcceptanceTestHelper.지하철_노선_목록_조회_요청(createResponse);

        // then
        LineAcceptanceTestHelper.지하철_노선_응답됨(response, createResponse);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        String name = "신분당선";
        ExtractableResponse<Response> createResponse = LineAcceptanceTestHelper.지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = LineAcceptanceTestHelper.지하철_노선_수정_요청(createResponse, lineRequest2);

        // then
        LineAcceptanceTestHelper.지하철_노선_수정됨(response);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = LineAcceptanceTestHelper.지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = LineAcceptanceTestHelper.지하철_노선_제거_요청(createResponse);

        // then
        LineAcceptanceTestHelper.지하철_노선_삭제됨(response);
    }
}
