package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static nextstep.subway.utils.FactoryMethods.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DijkstraPathFinderTest {
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private DijkstraPathFinder dijkstraPathFinder;

    /**
     * 교대역  --- *2호선* (10)--   강남역
     * |                        |
     * *3호선* (3)               *신분당선* (10)
     * |                        |
     * 남부터미널역 - *3호선* (2) -  양재
     */
    @BeforeEach
    void setUp() {
        강남역 = createStation("강남역");
        양재역 = createStation("양재역");
        교대역 = createStation("교대역");
        남부터미널역 = createStation("남부터미널역");

        신분당선 = createLine("신분당선", "rbg-red-600", 강남역, 양재역, 10);
        이호선 = createLine("이호선", "rbg-red-600", 교대역, 강남역, 10);
        삼호선 = createLine("삼호선", "rbg-red-600", 교대역, 양재역, 5);
        삼호선.addSection(createSection(교대역, 남부터미널역, 2));

        dijkstraPathFinder = new DijkstraPathFinder();
    }

    @Test
    @DisplayName("최단 경로 탐색")
    void 최단경로탐색() {
        Path path = dijkstraPathFinder.findShortestPathWithLines(Arrays.asList(신분당선, 이호선, 삼호선), 교대역, 양재역);

        //then
        assertAll(
                () -> assertThat(path.getStations()).containsExactly(교대역, 남부터미널역, 양재역),
                () -> assertThat(path.getDistance()).isEqualTo(5)
        );
    }

    @Test
    @DisplayName("최단 경로 탐색 실패 : 출발역과 도착역이 같은 경우")
    void 최단경로탐색_실패_출발역과_도착역이_같음() {
        //given
        DijkstraPathFinder dijkstraPathFinder = new DijkstraPathFinder();

        //then
        assertThrows(IllegalArgumentException.class,
                () -> dijkstraPathFinder.findShortestPathWithLines(Arrays.asList(신분당선, 이호선, 삼호선), 교대역, 교대역));
    }

    @Test
    @DisplayName("최단 경로 탐색 실패 : 출발역과 도착역이 연결이 되어 있지 않은 경우")
    void 최단경로탐색_실패_출발역과_도착역이_연결되지_않음() {
        //given
        Station 부평역 = createStation("부평역");
        Station 인천시청역 = createStation("인천시청역");
        Line 인천선 = createLine("신분당선", "rbg-red-600", 부평역, 인천시청역, 10);

        //then
        assertThrows(IllegalArgumentException.class,
                () -> dijkstraPathFinder.findShortestPathWithLines(Arrays.asList(신분당선, 이호선, 삼호선, 인천선), 교대역, 부평역));
    }

    @Test
    @DisplayName("최단 경로 탐색 실패 : 존재하지 않은 출발역이나 도착역을 조회 할 경우")
    void 최단경로탐색_실패_존재하지_않는_역() {
        //given
        Station 존재하지않는역 = createStation("존재하지않는역");

        //then
        assertThrows(IllegalArgumentException.class,
                () -> dijkstraPathFinder.findShortestPathWithLines(Arrays.asList(신분당선, 이호선, 삼호선), 교대역, 존재하지않는역));
    }
}
