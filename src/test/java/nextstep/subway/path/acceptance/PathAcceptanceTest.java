package nextstep.subway.path.acceptance;

import static nextstep.subway.line.acceptance.LineSectionTestFixture.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.line.acceptance.LineTestFixture.지하철_노선_등록되어_있음;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
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

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-orange-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(신분당선, 강남역, 양재역, 8);
        지하철_노선에_지하철역_등록되어_있음(이호선, 교대역, 강남역, 7);
        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 5);
        지하철_노선에_지하철역_등록되어_있음(삼호선, 남부터미널역, 양재역, 9);
    }

    @DisplayName("출발역과 도착역의 최단거리를 조회된다.")
    @Test
    void find_path() {
        //given
        //출발지 교대역
        //도착지 양재역

        //when
        //출발역에서 도착역 경로를 조회한다.

        //then
        //교대에서 남부터미널 양재로 조회된다.

    }

    @DisplayName("출발역과 도착역이 다른 경우를 조회환다.")
    @Test
    void different_source_target() {
        //given
        // 출발역 교대역
        // 도착역 교대역

        //when
        //출발지에서 도착역 경로를 조회한다.

        //then
        //출발지에서 도착지 경로 조회가 실패한다.
    }

    @DisplayName("출발역과 도착역이 연결된 경우를 조회한다.")
    @Test
    void linked_source_target() {
        //given
        // 지하철 역 등록
        // 지하철 역 등록
        // 지하철 노선 등록

        //when
        //출발지에서 도착역 경로를 조회한다.

        //then
        //출발지에서 도착지 경로 조회가 실패한다.
    }

    @DisplayName("존재하는 출발역과 도착역을 조회한다.")
    @Test
    void exist_source_target() {
        //given
        // 지하철 역 등록

        //when
        //출발지에서 도착역 경로를 조회한다.

        //then
        //출발지에서 도착지 경로 조회가 실패한다.

    }
}
