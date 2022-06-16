package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.exception.PathException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Path 단위 테스트")
class PathFinderTest {
    private PathFinder pathFinder;
    private Station 청담역;
    private Station 뚝섬유원지역;
    private Station 건대입구역;
    private Station 구의역;
    private Station 강변역;
    private Station 군자역;
    private Station 아차산역;
    private Station 중곡역;
    private Station 강남역;

    private Line 칠호선;
    private Line 일호선;
    private Line 이호선;
    private Line 삼호선;
    private Line 오호선;

    /**
     * 청담역 - 7호선(20)- 뚝섬유원지역 -7호선(10) - 건대입구역
     *                      |                       |
     *                    1호선 (5)              2호선(10)
     *                      |                       |
     *                    중곡역    - 3호선 (7) -   구의역
     *                                             |
     *                                          2호선(10)
     *                                             |
     *                                           강변역
     *
     * 군자역 - 5호선(20) - 아차산역
     */
    @BeforeEach
    void init() {
        pathFinder = new PathFinder();

        //7호선
        청담역 = new Station("청담역");
        뚝섬유원지역 = new Station("뚝섬유원지역");
        //2호선
        구의역 = new Station("구의역");
        강변역 = new Station("강변역");
        //7호선, 2호선
        건대입구역 = new Station("건대입구역");
        //5호선
        군자역 = new Station("군자역");
        아차산역 = new Station("아차산역");
        //1호선, 3호선
        중곡역 = new Station("중곡역");

        강남역 = new Station("강남역");

        //7호선
        칠호선 = new Line("칠호선", "yellow", 청담역, 뚝섬유원지역, 20);
        칠호선.addLineStation(뚝섬유원지역, 건대입구역, 10);

        //2호선
        이호선 = new Line("이호선", "green", 구의역, 강변역, 10);
        이호선.addLineStation(건대입구역, 구의역, 10);

        //5호선
        오호선 = new Line("오호선", "red", 군자역, 아차산역, 10);
        
        //1호선
        일호선 = new Line("일호선", "red", 뚝섬유원지역, 중곡역, 5);

        //3호선
        삼호선 = new Line("삼호선", "red", 중곡역, 구의역, 7);
    }

    @Test
    @DisplayName("주어진 지하철 노선에서, 여러 경로 중 최단 거리인 경로를 조회한다.(HappyPath)")
    void getShortestPath() {
        //when
        pathFinder.addPathForLines(Arrays.asList(칠호선, 이호선, 오호선, 일호선, 삼호선));

        //given
        Path path = pathFinder.getShortestPath(청담역, 강변역);

        //then
        //Distance 기준으로 가장 짧은 구간 리스트
        assertThat(path.getStations()).containsExactly(청담역, 뚝섬유원지역, 중곡역, 구의역, 강변역);
        assertThat(path.getDistance()).isEqualTo(42);
    }

    @Test
    void 출발역과_도착역이_연결되어있지_않은경우() {
        //when
        pathFinder.addPathForLines(Arrays.asList(칠호선, 이호선, 오호선, 일호선, 삼호선));

        //then
        assertThatThrownBy(() -> {
            pathFinder.getShortestPath(청담역, 군자역);
        }).isInstanceOf(PathException.class)
        .hasMessageContaining(PathException.PATH_FIND_NO_SEARCH_MSG);
    }

    @Test
    void 출발역과_도착역이_같으면_조회되지_않는다() {
        //when
        pathFinder.addPathForLines(Arrays.asList(칠호선, 이호선, 오호선, 일호선, 삼호선));

        //then
        assertThatThrownBy(() -> {
            pathFinder.getShortestPath(청담역, 청담역);
        }).isInstanceOf(PathException.class)
        .hasMessageContaining(PathException.SAME_SOURCE_TARGET_STATION_MSG);
    }

    @Test
    void 출발역이나_도착역이_존재하지_않는_경우() {
        //when
        pathFinder.addPathForLines(Arrays.asList(칠호선, 이호선, 오호선, 일호선, 삼호선));

        //given
        assertThatThrownBy(() -> {
            pathFinder.getShortestPath(강남역, 청담역);
        }).isInstanceOf(PathException.class)
        .hasMessageContaining(PathException.NO_CONTAIN_STATION_MSG);
    }
}