package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;

import static nextstep.subway.station.domain.StationTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PathFinderTest {
    private Line 이호선;
    private Line 삼호선;
    private Line 오호선;
    private Line 신분당선;
    /**
     * *3호선*
     * |             10                   10        10
     * 교대역 -------------------  강남역  ------ 역삼역 -----  선릉  *2호선*  삼성역(역삼역과 연결 공사중)
     * |                           |
     * |  2                    10  |
     * |              3            |
     * 남부터미널역  --------------   양재 --*3호선*
     *                          10 |
     *                             |
     *                             |
     *                          양재시민의숲역
     *                          *신분당선*
     *
     *
     *   5호선  목동역 ----- 오목교역
     */
    @BeforeEach
    void setup() {
        이호선 = new Line("2호선", "green", 교대역, 강남역, 10);
        이호선.addSection(강남역, 역삼역, 10);
        이호선.addSection(역삼역, 선릉역, 10);

        신분당선 = new Line("신분당선", "red", 강남역, 양재역, 10);
        신분당선.addSection(양재역, 양재시민의숲역, 10);

        삼호선 = new Line("삼호선", "orange", 교대역, 남부터미널역, 2);
        삼호선.addSection(남부터미널역, 양재역, 3);

        오호선 = new Line("오호선", "orange", 목동역, 오목교역, 5);
    }

    @DisplayName("최단 경로 조회")
    @Test
    void findTest() {
        PathFinder graph = PathFinder.of(asList(이호선, 신분당선, 삼호선));

        // when
        Path subwayPath = graph.findShortestPath(양재시민의숲역, 선릉역);

        // then
        assertThat(subwayPath.getStations()).containsExactly(양재시민의숲역, 양재역, 강남역, 역삼역, 선릉역);
        assertThat(subwayPath.sumPathDistance()).isEqualTo(40);
    }

    @DisplayName("출발역과 도착역이 같은 경우")
    @Test
    void equalSourceAndTarget() {
        PathFinder graph = PathFinder.of(asList(이호선, 신분당선, 삼호선));
        assertThatThrownBy(() -> graph.findShortestPath(선릉역, 선릉역))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우")
    @Test
    void notCennected() {
        Line 오호선 = new Line("오호선","purple", 목동역, 오목교역, 10);
        PathFinder graph = PathFinder.of(asList(이호선, 신분당선, 삼호선, 오호선));
        assertThatThrownBy(() -> graph.findShortestPath(선릉역, 선릉역))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("존재하지 않는 출발역과 도착역을 조회할 경우")
    @Test
    void nonExistsStations() {
        PathFinder graph = PathFinder.of(asList(이호선, 신분당선, 삼호선));
        assertThatThrownBy(() -> graph.findShortestPath(도곡역, 서초역))
                .isInstanceOf(RuntimeException.class);
    }
}