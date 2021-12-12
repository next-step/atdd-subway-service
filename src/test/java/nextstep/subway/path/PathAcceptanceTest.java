package nextstep.subway.path;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTestHelper;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTestHelper;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTestHelper;
import nextstep.subway.station.dto.StationResponse;

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
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTestHelper.지하철역_등록되어_있음("강남역")
            .as(StationResponse.class);
        양재역 = StationAcceptanceTestHelper.지하철역_등록되어_있음("양재역")
            .as(StationResponse.class);
        교대역 = StationAcceptanceTestHelper.지하철역_등록되어_있음("교대역")
            .as(StationResponse.class);
        남부터미널역 = StationAcceptanceTestHelper.지하철역_등록되어_있음("남부터미널역")
            .as(StationResponse.class);

        신분당선 = LineAcceptanceTestHelper.지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 7)
            .as(LineResponse.class);
        이호선 = LineAcceptanceTestHelper.지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 2)
            .as(LineResponse.class);
        삼호선 = LineAcceptanceTestHelper.지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 8)
            .as(LineResponse.class);

        LineSectionAcceptanceTestHelper.지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("최단 경로를 조회한다.")
    @Test
    void getPath() {
        // given
        List<StationResponse> stations = Arrays.asList(교대역, 남부터미널역, 양재역);
        PathResponse pathResponse = new PathResponse(stations, 8);

        // when
        ExtractableResponse<Response> response = PathAcceptanceTestHelper.최단_경로_조회_요청(교대역.getId(), 양재역.getId());

        // then
        PathAcceptanceTestHelper.최단_경로_조회_응답됨(response);
        PathAcceptanceTestHelper.최단경로_조회_예상된_결과_응답됨(response, pathResponse);
    }

    @DisplayName("출발역과 도착역이 같은 최단 경로를 조회한다.")
    @Test
    void getPathWithSameSourceAndTarget() {
        // given

        // when
        ExtractableResponse<Response> response = PathAcceptanceTestHelper.최단_경로_조회_요청(교대역.getId(), 교대역.getId());

        // then
        PathAcceptanceTestHelper.최단_경로_조회_실패됨(response);
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 최단 경로를 조회한다.")
    @Test
    void getPathWithNotConnectedSourceAndTarget() {
        // given
        StationResponse 정자역 = StationAcceptanceTestHelper.지하철역_등록되어_있음("정자역")
            .as(StationResponse.class);
        StationResponse 미금역 = StationAcceptanceTestHelper.지하철역_등록되어_있음("미금역")
            .as(StationResponse.class);
        LineAcceptanceTestHelper.지하철_노선_등록되어_있음("분당선", "bg-yellow-600", 정자역, 미금역, 8);

        // when
        ExtractableResponse<Response> response = PathAcceptanceTestHelper.최단_경로_조회_요청(교대역.getId(), 정자역.getId());

        // then
        PathAcceptanceTestHelper.최단_경로_조회_실패됨(response);
    }
}
