package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import nextstep.subway.exception.InvalidArgumentException;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 경로 찾기 테스트")
public class PathFinderTest {

    private static final Station 교대역 = spy(new Station("교대역"));
    private static final Station 강남역= spy(new Station("강남역"));
    private static final Station 양재역= spy(new Station("양재역"));
    private static final Station 남부터미널역= spy(new Station("남부터미널역"));
    private static final Station 양재시민의숲= spy(new Station("양재시민의숲"));
    private static final Station 서초역= spy(new Station("서초역"));
    private static final Station 고속터미널= spy(new Station("고속터미널"));
    private static final Station 반포역= spy(new Station("반포역"));

    static {
        when(교대역.getId()).thenReturn(1L);
        when(강남역.getId()).thenReturn(2L);
        when(양재역.getId()).thenReturn(3L);
        when(남부터미널역.getId()).thenReturn(4L);
        when(양재시민의숲.getId()).thenReturn(5L);
        when(서초역.getId()).thenReturn(6L);
        when(고속터미널.getId()).thenReturn(7L);
        when(반포역.getId()).thenReturn(8L);
    }

    Line 신분당선;
    Line 이호선;
    Line 삼호선;
    Line 칠호선;


    /**
     *                     고속터미널--- *7호선* 5 -----  반포역
     *                        |
     *                     *3호선* 3
     *                        |
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
        신분당선 = Line.of("신분당선", "red", 강남역, 양재역, 10);
        신분당선.addLineStation(Section.create(양재역, 양재시민의숲, Distance.valueOf(4)));

        이호선 = Line.of("이호선", "green", 교대역, 강남역, 10);
        이호선.addLineStation(Section.create(서초역, 교대역, Distance.valueOf(5)));

        삼호선 = Line.of("삼호선", "orange", 교대역, 남부터미널역, 3);
        삼호선.addLineStation(Section.create(남부터미널역, 양재역, Distance.valueOf(2)));
        삼호선.addLineStation(Section.create(고속터미널, 교대역, Distance.valueOf(3)));

        칠호선 = Line.of("칠호선", "olive", 고속터미널, 반포역, 5);
    }


    @Test
    @DisplayName("교대역-양재역 지하철 최단 경로 조회")
    void getShortestList_교대역_양재역() {

        PathFinder graph = PathFinder.createWeightMultiGraph(Arrays.asList(신분당선, 이호선, 삼호선));

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
        when(강남역.getId()).thenReturn(1L);
        PathFinder graph = PathFinder.createWeightMultiGraph(Arrays.asList(신분당선, 이호선, 삼호선));

        Path shortestPath = graph.findShortestPath(서초역, 양재시민의숲);

        assertAll(() -> {
            assertThat(shortestPath.getTotalDistance()).isEqualTo(14);
            assertThat(shortestPath.getStations()).extracting(Station::getName)
                .containsExactly("서초역", "교대역", "남부터미널역", "양재역", "양재시민의숲" );
        });
    }

    @Test
    @DisplayName("최단 경로 조회 시 출발역과 도착역이 같은 경우 InvalidArgumentException 발생")
    void getShortestListSameFromToFail() {

        PathFinder graph = PathFinder.createWeightMultiGraph(Arrays.asList(신분당선));

        assertThatThrownBy(() -> graph.findShortestPath(양재역, 양재역))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessage("출발역과 도착역이 같습니다.");
    }

    @Test
    @DisplayName("출발역과 도착역이 연결되어 있지 경우 InvalidArgumentException 발생")
    void getShortestListConnectedFail() {

        PathFinder graph = PathFinder.createWeightMultiGraph(Arrays.asList(신분당선, 칠호선));

        assertThatThrownBy(() -> graph.findShortestPath(반포역, 강남역))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessage("출발역과 도착역이 연결되어 있지 않습니다.");
    }

    @Test
    @DisplayName("출발역, 도착역이 존재하지 않는 경우 NotFoundException 발생")
    void getShortestListNoFoundStationFail() {

        PathFinder graph = PathFinder.createWeightMultiGraph(Arrays.asList(이호선, 삼호선));

        assertThatThrownBy(() -> graph.findShortestPath(양재시민의숲, 강남역))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("출발역 또는 도착역이 존재하지 않습니다.");

        assertThatThrownBy(() -> graph.findShortestPath(서초역, 반포역))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("출발역 또는 도착역이 존재하지 않습니다.");

        assertThatThrownBy(() -> graph.findShortestPath(반포역, 양재시민의숲))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("출발역 또는 도착역이 존재하지 않습니다.");
    }
}
