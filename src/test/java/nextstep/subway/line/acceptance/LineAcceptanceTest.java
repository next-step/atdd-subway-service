package nextstep.subway.line.acceptance;

import static nextstep.subway.line.acceptance.LineAcceptanceMethods.*;
import static nextstep.subway.station.StationAcceptanceMethods.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("지하철 노선 관련 기능")
class LineAcceptanceTest extends AcceptanceTest {
    private static final int DISTANCE_10 = 10;
    private static final int DISTANCE_15 = 15;
    private static final int FARE_1000 = 1000;
    private static final int FARE_900 = 900;

    private StationResponse 강남역;
    private StationResponse 광교역;
    private LineRequest 신분당선_Request;
    private LineRequest 구신분당선_Request;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = 지하철역_등록되어_있음(StationRequest.of("강남역")).as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음(StationRequest.of("광교역")).as(StationResponse.class);

        신분당선_Request = LineRequest.of("신분당선", "bg-red-600", FARE_1000, 강남역.getId(), 광교역.getId(), DISTANCE_10);
        구신분당선_Request = LineRequest.of("구신분당선", "bg-red-600", FARE_900, 강남역.getId(), 광교역.getId(), DISTANCE_15);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선_Request);

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        지하철_노선_등록되어_있음(신분당선_Request);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선_Request);

        // then
        지하철_노선_생성_실패됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> 신분당선_Response = 지하철_노선_등록되어_있음(신분당선_Request);
        ExtractableResponse<Response> 구신분당선_Response = 지하철_노선_등록되어_있음(구신분당선_Request);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_응답됨(response);
        지하철_노선_목록_포함됨(response, Arrays.asList(신분당선_Response, 구신분당선_Response));
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_등록되어_있음(신분당선_Request);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청(createResponse);

        // then
        지하철_노선_응답됨(response, createResponse);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        String name = "신분당선";
        ExtractableResponse<Response> createResponse = 지하철_노선_등록되어_있음(신분당선_Request);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(createResponse, 구신분당선_Request);

        // then
        지하철_노선_수정됨(response);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_등록되어_있음(신분당선_Request);

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(createResponse);

        // then
        지하철_노선_삭제됨(response);
    }
}
