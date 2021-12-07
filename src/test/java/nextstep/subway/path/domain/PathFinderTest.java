package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 경로 찾기 테스트")
public class PathFinderTest {

    Station 교대역;
    Station 강남역;
    Station 양재역;
    Station 남부터미널역;
    Station 양재시민의숲;
    Station 서초역;

    Line 신분당선;
    Line 이호선;
    Line 삼호선;


    /**
     * 서초역  --- *2호선* 5--- 교대역  --- *2호선* 10 -----  강남역
     *                        |                          |
     *                      *3호선* 3                 *신분당선* 10
     *                        |                          |
     *                      남부터미널역  --- *3호선* 2 --- 양재
     *                                                  |
     *                                               *신분당선* 4
     *                                                  |
     *                                               양재시민의숲
     */

    @BeforeEach
    void setUp() {
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");
        양재시민의숲 = new Station("양재시민의숲");
        서초역 = new Station("서초역");

        신분당선 = Line.of("신분당선", "red", 강남역, 양재역, 10);
        신분당선.addLineStation(Section.create(양재역, 양재시민의숲, Distance.valueOf(4)));

        이호선 = Line.of("이호선", "green", 교대역, 강남역, 10);
        이호선.addLineStation(Section.create(서초역, 교대역, Distance.valueOf(5)));

        삼호선 = Line.of("삼호선", "orange", 교대역, 남부터미널역, 3);
        삼호선.addLineStation(Section.create(남부터미널역, 양재역, Distance.valueOf(2)));
    }


    @Test
    @DisplayName("교대역-양재역 지하철 최단 경로 조회")
    void getShortestList_교대역_양재역() {
        PathFinder graph = PathFinder.of(Arrays.asList(신분당선, 이호선, 삼호선));

        Path shortestPath = graph.findShortestPath(교대역, 양재역);

        assertAll(() -> {
            assertThat(shortestPath.getTotalDistance()).isEqualTo(5);
            assertThat(shortestPath.getStations())
                .extracting(Station::getName).containsExactly("교대역", "남부터미널역", "양재역" );
        });
    }

    @Test
    @DisplayName("서초역_양재시민의숲 지하철 최단 경로 조회")
    void getShortestList_서초역_양재시민의숲() {
        PathFinder graph = PathFinder.of(Arrays.asList(신분당선, 이호선, 삼호선));

        Path shortestPath = graph.findShortestPath(서초역, 양재시민의숲);

        assertAll(() -> {
            assertThat(shortestPath.getTotalDistance()).isEqualTo(14);
            assertThat(shortestPath.getStations()).extracting(Station::getName)
                .containsExactly("서초역", "교대역", "남부터미널역", "양재역", "양재시민의숲" );
        });
    }
}
