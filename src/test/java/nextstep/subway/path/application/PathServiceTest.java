package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class PathServiceTest {

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    private Line 이호선;
    private Line 삼호선;
    private Line 신분당선;

    private List<Line> lines;


    @MockBean
    private StationService stationService;

    @MockBean
    private LineService lineService;

    /**
     * 교대역    --- *2호선* (10) ---    강남역
     * |                                  |
     * *3호선(5)*                     *신분당선* (6)
     * |                                |
     * 남부터미널역 --- *3호선 (3)* ---   양재
     */

    @BeforeEach
    public void setUp() {
        강남역 = new Station(1L, "강남역");
        양재역 = new Station(2L, "양재역");
        교대역 = new Station(3L, "교대역");
        남부터미널역 = new Station(4L, "남부터미널역");

        이호선 = new Line("이호선", "bg-green-600", 교대역, 강남역, 10, 0);
        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 6, 0);
        삼호선 = new Line("삼호선", "bg-orange-600", 교대역, 남부터미널역, 5, 0);

        삼호선.addSection(new Section(삼호선, 남부터미널역, 양재역, 3));

        lines = new ArrayList<>();
        lines.add(이호선);
        lines.add(삼호선);
        lines.add(신분당선);
    }

    @DisplayName("최단 경로를 조회한다.")
    @Test
    void getDijkstraShortestPath() {
        // given
        PathService pathService = new PathService(lineService, stationService);

        given(lineService.findAll()).willReturn(lines);
        given(stationService.findStationById(남부터미널역.getId())).willReturn(남부터미널역);
        given(stationService.findStationById(강남역.getId())).willReturn(강남역);

        // when
        PathResponse pathResponse = pathService.findShortestPath(남부터미널역.getId(), 강남역.getId(), 30);

        // then
        assertEquals(9, pathResponse.getDistance());
    }

    /**
     * Given 양재역에서 판교역을 연결하는 14km 구분당선을 추가 후
     * When  강남역에서 판교역의 최단 경로 요금을 조회하면
     * Then  1350원의 요금이 조회된다
     */
    /**
     * 교대역    --- *2호선* (10) ---    강남역
     * |                                  |
     * *3호선(5)*                     *신분당선* (6)
     * |                                |
     * 남부터미널역 --- *3호선 (3)* ---   양재 -*구분당선(14)*- 판교역
     */
    @DisplayName("10km 초과 50km 이하 거리 요금을 조회한다.")
    @Test
    void getFare10km_50km() {
        // given
        Station 판교역 = new Station(5L, "판교역");
        Line 구분당선 = new Line("구분당선", "bg-red-300", 양재역, 판교역, 14, 0);
        lines.add(구분당선);

        PathService pathService = new PathService(lineService, stationService);

        given(lineService.findAll()).willReturn(lines);
        given(stationService.findStationById(양재역.getId())).willReturn(양재역);
        given(stationService.findStationById(판교역.getId())).willReturn(판교역);

        // when
        PathResponse pathResponse = pathService.findShortestPath(양재역.getId(), 판교역.getId(), 30);

        // then
        assertEquals(1350, pathResponse.getFare());
    }

    /**
     * Given 양재역에서 판교역을 연결하는 54km 구분당선을 추가 후
     * When  강남역에서 판교역의 최단 경로 요금을 조회하면
     * Then  2250원의 요금이 조회된다
     */
    /**
     * 교대역    --- *2호선* (10) ---    강남역
     * |                                  |
     * *3호선(5)*                     *신분당선* (6)
     * |                                |
     * 남부터미널역 --- *3호선 (3)* ---   양재 -*구분당선(54)*- 판교역
     */
    @DisplayName("50km 초과 거리 요금을 조회한다.")
    @Test
    void getFareOver50km() {
        // given
        Station 판교역 = new Station(5L, "판교역");
        Line 구분당선 = new Line("구분당선", "bg-red-300", 양재역, 판교역, 54, 0);
        lines.add(구분당선);

        PathService pathService = new PathService(lineService, stationService);

        given(lineService.findAll()).willReturn(lines);
        given(stationService.findStationById(강남역.getId())).willReturn(강남역);
        given(stationService.findStationById(판교역.getId())).willReturn(판교역);

        // when
        PathResponse pathResponse = pathService.findShortestPath(강남역.getId(), 판교역.getId(), 30);

        // then
        assertEquals(2250, pathResponse.getFare());
    }

    /**
     * Given 양재역에서 판교역을 연결하는 5km 구분당선을 추가 후
     *       And 노선 추가요금 1000원을 지정하고
     * When  강남역에서 판교역의 최단 경로 요금을 조회하면
     * Then  2350원의 요금이 조회된다
     */
    /**
     * 교대역    --- *2호선* (10) ---    강남역
     * |                                  |
     * *3호선(5)*                     *신분당선* (6)
     * |                                |
     * 남부터미널역 --- *3호선 (3)* ---   양재 -*구분당선(5), 추가요금 1000원 *- 판교역
     */
    @DisplayName("라인 추가요금이 반영된 요금을 조회한다.")
    @Test
    void getFareWithLineSurcharge() {
        // given
        Station 판교역 = new Station(5L, "판교역");
        Line 구분당선 = new Line("구분당선", "bg-red-300", 양재역, 판교역, 5, 1000);
        lines.add(구분당선);

        PathService pathService = new PathService(lineService, stationService);

        given(lineService.findAll()).willReturn(lines);
        given(stationService.findStationById(강남역.getId())).willReturn(강남역);
        given(stationService.findStationById(판교역.getId())).willReturn(판교역);

        // when
        PathResponse pathResponse = pathService.findShortestPath(강남역.getId(), 판교역.getId(), 30);

        // then
        assertEquals(2350, pathResponse.getFare());
    }

    /**
     * Given 양재역에서 판교역을 연결하는 5km 구분당선을 추가 후
     *       And 노선 추가요금 1000원을 지정하고
     *       And 판교역에서 이매역을 연결하는 5km 경강선을 추가한 후
     *       And 노선 추가 요금을 2000원으로 지정하고
     * When  강남역에서 이매역의 최단 경로 요금을 조회하면
     * Then  3450원의 요금이 조회된다
     */
    /**
     * 교대역    --- *2호선* (10) ---    강남역                                 이매역
     * |                                  |                                     |
     * *3호선(5)*                     *신분당선* (6)                         *경강선(5), 추가요금 2000원*
     * |                                 |                                      |
     * 남부터미널역 --- *3호선 (3)* ---   양재 -*구분당선(5), 추가요금 1000원 *- 판교역
     */
    @DisplayName("추가요금이 있는 라인을 환승할 경우 추가요금의 최대 요금을 반영한다.")
    @Test
    void getFareWithMultipleLineSurcharge() {
        // given
        Station 판교역 = new Station(5L, "판교역");
        Line 구분당선 = new Line("구분당선", "bg-red-300", 양재역, 판교역, 5, 1000);
        lines.add(구분당선);

        Station 이매역 = new Station(6L, "이매역");
        Line 경강선 = new Line("경강선", "bg-blue-300", 판교역, 이매역, 5, 2000);
        lines.add(경강선);

        PathService pathService = new PathService(lineService, stationService);

        given(lineService.findAll()).willReturn(lines);
        given(stationService.findStationById(강남역.getId())).willReturn(강남역);
        given(stationService.findStationById(이매역.getId())).willReturn(이매역);

        // when
        PathResponse pathResponse = pathService.findShortestPath(강남역.getId(), 이매역.getId(), 30);

        // then
        assertEquals(3450, pathResponse.getFare());
    }

    /**
     * Given 양재역에서 판교역을 연결하는 5km 구분당선을 추가 후
     *       And 노선 추가요금 1000원을 지정하고
     *       And 나이를 12세(어린이)로 지정 후
     * When  강남역에서 판교역의 최단 경로 요금을 조회하면
     * Then  1350원의 요금이 조회된다
     */
    /**
     * 교대역    --- *2호선* (10) ---    강남역
     * |                                  |
     * *3호선(5)*                     *신분당선* (6)
     * |                                |
     * 남부터미널역 --- *3호선 (3)* ---   양재 -*구분당선(5), 추가요금 1000원 *- 판교역
     */
    @DisplayName("라인 추가요금이 반영된 어린이 요금을 조회한다.")
    @Test
    void getFareWithLineSurchargeForChildren() {
        // given
        Station 판교역 = new Station(5L, "판교역");
        Line 구분당선 = new Line("구분당선", "bg-red-300", 양재역, 판교역, 5, 1000);
        lines.add(구분당선);

        PathService pathService = new PathService(lineService, stationService);

        given(lineService.findAll()).willReturn(lines);
        given(stationService.findStationById(강남역.getId())).willReturn(강남역);
        given(stationService.findStationById(판교역.getId())).willReturn(판교역);

        // when
        PathResponse pathResponse = pathService.findShortestPath(강남역.getId(), 판교역.getId(), 12);

        // then
        assertEquals(1350, pathResponse.getFare());
    }

    /**
     * Given 양재역에서 판교역을 연결하는 5km 구분당선을 추가 후
     *       And 노선 추가요금 1000원을 지정하고
     *       And 나이를 13세(청소년)로 지정 후
     * When  강남역에서 판교역의 최단 경로 요금을 조회하면
     * Then  1950원의 요금이 조회된다
     */
    /**
     * 교대역    --- *2호선* (10) ---    강남역
     * |                                  |
     * *3호선(5)*                     *신분당선* (6)
     * |                                |
     * 남부터미널역 --- *3호선 (3)* ---   양재 -*구분당선(5), 추가요금 1000원 *- 판교역
     */
    @DisplayName("라인 추가요금이 반영된 청소년 요금을 조회한다.")
    @Test
    void getFareWithLineSurchargeForTeenagers() {
        // given
        Station 판교역 = new Station(5L, "판교역");
        Line 구분당선 = new Line("구분당선", "bg-red-300", 양재역, 판교역, 5, 1000);
        lines.add(구분당선);

        PathService pathService = new PathService(lineService, stationService);

        given(lineService.findAll()).willReturn(lines);
        given(stationService.findStationById(강남역.getId())).willReturn(강남역);
        given(stationService.findStationById(판교역.getId())).willReturn(판교역);

        // when
        PathResponse pathResponse = pathService.findShortestPath(강남역.getId(), 판교역.getId(), 13);

        // then
        assertEquals(1950, pathResponse.getFare());
    }
}
