package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathStation;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class PathFinderTest {
    // given
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 경의중앙선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 용산역;
    private Station 공덕역;
    private PathFinder pathFinder;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    void setUp() {
        // given
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        용산역 = new Station("용산역");
        공덕역 = new Station("공덕역");

        신분당선 = new Line("신분당선", "green", 강남역, 양재역, 10);
        이호선 = new Line("2호선", "green", 교대역, 강남역, 10);
        삼호선 = new Line("2호선", "green", 교대역, 양재역, 5);
        경의중앙선 = new Line("경의중앙선", "skyblue", 공덕역, 용산역, 10);

        삼호선.addLineStation(교대역, 남부터미널역, 3);

        List<Line> lines = Arrays.asList(신분당선, 이호선, 삼호선, 경의중앙선);
        List<Section> sections = findSectionsInLines(lines);
        pathFinder = new PathFinder(sections);
    }

    @Test
    @DisplayName("지하철 구간 최단경로를 조회한다.")
    public void findShortestPath() throws Exception {
        // when
        List<PathStation> path = pathFinder.findPath(PathStation.of(교대역), PathStation.of(양재역));

        // then
        assertThat(path).containsExactly(PathStation.of(교대역), PathStation.of(남부터미널역), PathStation.of(양재역));
    }

    @Test
    @DisplayName("지하철 구간 최단거리를 조회한다.")
    public void findShortestDistance() throws Exception {
        // when
        Distance shortestDistance = pathFinder.findShortestDistance(PathStation.of(교대역), PathStation.of(양재역));

        // then
        assertThat(5).isEqualTo(shortestDistance.get());
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우 오류가 발생한다.")
    public void sameSourceTarget() throws Exception {
        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> pathFinder.findPath(PathStation.of(교대역), PathStation.of(교대역)))
                .withMessageMatching("출발역과 도착역이 같습니다.");
    }

    @Test
    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 오류가 발생한다.")
    public void notEdgeSourceTarget() throws Exception {
        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> pathFinder.findPath(PathStation.of(교대역), PathStation.of(공덕역)))
                .withMessageMatching("출발역과 도착역이 연결이 되어 있지 않습니다.");
    }

    private List<Section> findSectionsInLines(List<Line> lines) {
        return lines.stream()
                .flatMap(line -> line.getSections().stream())
                .collect(Collectors.toList());
    }
}
