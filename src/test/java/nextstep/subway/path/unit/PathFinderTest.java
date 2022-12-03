package nextstep.subway.path.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import nextstep.subway.ErrorMessage;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.DijkstraShortestPathStrategy;
import nextstep.subway.path.domain.StationGraph;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathFinderTest {
    private final Line 이호선 = Line.ofNameAndColor("2호선", "초록색");
    private final Line 신분당선 = Line.ofNameAndColor("신분당선", "빨간색");
    private final Line 삼호선 = Line.ofNameAndColor("3호선", "주황색");
    private final Station 강남역 = new Station("강남역");
    private final Station 양재역 = new Station("양재역");
    private final Station 교대역 = new Station("교대역");
    private final Station 남부터미널역 = new Station("남부터미널역");
    private final Section 강남역_양재역 = Section.of(신분당선, 강남역, 양재역, 10);
    private final Section 교대역_강남역 = Section.of(이호선, 교대역, 강남역, 10);
    private final Section 교대역_남부터미널역 = Section.of(삼호선, 교대역, 남부터미널역, 7);
    private final Section 남부터미널역_양재역 = Section.of(삼호선, 남부터미널역, 양재역, 5);


    @DisplayName("최단 경로를 구한다.")
    @Test
    void findShortestPath() {
        // given
        StationGraph stationGraph = new StationGraph(new DijkstraShortestPathStrategy(), Arrays.asList(강남역_양재역, 교대역_강남역, 교대역_남부터미널역, 남부터미널역_양재역));
        PathFinder pathFinder = PathFinder.of(stationGraph);

        // when
        Path path = pathFinder.findPathFromGraph(강남역, 남부터미널역);

        // then
        assertThat(path.getStations()).containsExactly(강남역, 양재역, 남부터미널역);
        assertThat(path.getDistance().value()).isEqualTo(15);
    }

    @DisplayName("최단 경로를 구할때 두개 역이 같으면 IllegalStateException 발생한다.")
    @Test
    void findShortestPath_exception_same_station() {
        // given
        StationGraph stationGraph = new StationGraph(new DijkstraShortestPathStrategy(), Arrays.asList(강남역_양재역, 교대역_강남역, 교대역_남부터미널역, 남부터미널역_양재역));
        PathFinder pathFinder = PathFinder.of(stationGraph);

        // then
        assertThatThrownBy(() -> pathFinder.findPathFromGraph(강남역, 강남역)).isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.FIND_PATH_SAME_STATION);
    }





}
