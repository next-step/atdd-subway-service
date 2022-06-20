package nextstep.subway.path.acceptance;

import static nextstep.subway.line.acceptance.LineAcceptanceSteps.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceSteps.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.path.acceptance.PathAcceptanceSteps.*;
import static nextstep.subway.station.StationAcceptanceSteps.지하철역_등록되어_있음;

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


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-orange-600", 남부터미널역.getId(), 양재역.getId(), 3)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 5);
    }

    @Test
    @DisplayName("최단 경로 조회")
    public void findShortestPath() {
        // when
        ExtractableResponse<Response> response = 지하철_최단_경로_조회(교대역.getId(), 양재역.getId());

        // then
        지하철_최단_경로_조회됨(response);
        지하철_최단_경로_지하철역_순서_정렬됨(response, Arrays.asList(교대역, 남부터미널역, 양재역));
        지하철_최단_경로_거리(response, 8);
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우 {INTERNAL_SERVER_ERROR}")
    public void findPathSameSourceAndTarget() {
        // when
        ExtractableResponse<Response> response = 지하철_최단_경로_조회(교대역.getId(), 교대역.getId());

        // then
        지하철_최단_경로_조회_실패됨(response);
    }

    @Test
    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 {INTERNAL_SERVER_ERROR}")
    public void notConnectSourceAndTarget() {
        // given
        StationResponse 서울역 = 지하철역_등록되어_있음("서울역").as(StationResponse.class);
        StationResponse 삼각지역 = 지하철역_등록되어_있음("삼각지역").as(StationResponse.class);
        LineResponse 사호선 = 지하철_노선_등록되어_있음(new LineRequest("4호선", "bg-blue-600", 서울역.getId(), 삼각지역.getId(), 8)).as(LineResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_최단_경로_조회(교대역.getId(), 삼각지역.getId());

        // then
        지하철_최단_경로_조회_실패됨(response);
    }

    @Test
    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우 {INTERNAL_SERVER_ERROR}")
    public void findPathNoExistStation() {
        // given
        Long 존재하지않는역 = 4885L;

        // when
        ExtractableResponse<Response> response = 지하철_최단_경로_조회(교대역.getId(), 존재하지않는역);

        // then
        지하철_최단_경로_조회_실패됨(response);
    }
}
