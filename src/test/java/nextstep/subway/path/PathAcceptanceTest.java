package nextstep.subway.path;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역을_등록;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        신분당선 = LineAcceptanceTest.지하철_노선_등록_요청(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10));
        이호선 = LineAcceptanceTest.지하철_노선_등록_요청(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10));
        삼호선 = LineAcceptanceTest.지하철_노선_등록_요청(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5));

        지하철_노선에_지하철역을_등록(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("최단 경로를 조회한다.")
    @Test
    void findBestPathTest() {
        //TODO
        /*
        2. 출발역과 도착역간의 최단경로를 조회할 수 있다
            - 출발역과 도착역이 같으면 실패한다
            - 출발역과 도착역이 연결이 되어 있지 않으면 실패한다
            - 존재하지 않은 출발역이나 도착역을 조회 할 경우 실패한다.
        */

        //when



    }
}
