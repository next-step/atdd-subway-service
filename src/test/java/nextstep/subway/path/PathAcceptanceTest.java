package nextstep.subway.path;

import static nextstep.subway.line.acceptance.LineAcceptanceSupport.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.SectionAcceptanceSupport.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.path.PathAcceptanceSupport.지하철역_최단거리_경로_검증_완료;
import static nextstep.subway.path.PathAcceptanceSupport.지하철역_최단거리_경로_조회_성공;
import static nextstep.subway.path.PathAcceptanceSupport.지하철역_최단거리_길이_검증_완료;
import static nextstep.subway.path.PathAcceptanceSupport.지하철역_최단경로_조회_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("지하철 경로 조회 인수 테스트")
class PathAcceptanceTest extends AcceptanceTest {
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

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 12)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    /**
     * Feature: 지하철 구간 관련 기능
     *
     *   Given 지하철역 등록되어 있음
     *   And 지하철 노선 등록되어 있음
     *   And 지하철 노선에 지하철역 등록되어 있음
     *
     *  Scenario: 지하철 최단경로를 조회
     *    Given 출발역, 도착역을 전달해 지하철역의 최단경로를 조회하면
     *    Then: 최단경로가 조회됨
    **/
    @DisplayName("지하철 최단 경로 조회 기능")
    @Test
    void find_shortest_path_test() {
        ExtractableResponse<Response> response = 지하철역_최단경로_조회_요청(강남역.getId(), 양재역.getId());
        지하철역_최단거리_경로_조회_성공(response);

        지하철역_최단거리_경로_검증_완료(response, Arrays.asList("강남역", "양재역"));
        지하철역_최단거리_길이_검증_완료(response, 12);
    }

    @DisplayName("지하철 환승역 포함 최단 경로 조회 기능")
    @Test
    void find_shortest_path_test2() {
        ExtractableResponse<Response> response = 지하철역_최단경로_조회_요청(강남역.getId(), 남부터미널역.getId());
        지하철역_최단거리_경로_조회_성공(response);

        지하철역_최단거리_길이_검증_완료(response, 13);
        지하철역_최단거리_경로_검증_완료(response, Arrays.asList("강남역", "교대역", "남부터미널역"));
    }
}
