package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathFinderTest {
    private Station 공덕역;
    private Station 영등포구청역;
    private Station 당산역;
    private Station 합정역;
    private Station 동래역;
    private Station 서면역;
    private List<Sections> sections;
    private PathFinder pathFinder;

    /**
     * 합정역 --- *6호선(4)* --- 공덕역
     * | |
     * *2호선(3)* *5호선(6)*
     * | |
     * 당산역 --- *2호선(5)* --- 영등포구청역
     */
    @BeforeEach
    void setUp() {
        공덕역 = new Station("공덕역");
        영등포구청역 = new Station("영등포구청역");
        당산역 = new Station("당산역");
        합정역 = new Station("합정역");
        동래역 = new Station("동래역");
        서면역 = new Station("서면역");

        Line 육호선 = Line.of("6호선", "bg-red-600", 합정역, 공덕역, Distance.from(4));
        Line 이호선 = Line.of("2호선", "bg-red-600", 합정역, 영등포구청역, Distance.from(8));
        Line 오호선 = Line.of("5호선", "bg-red-600", 공덕역, 영등포구청역, Distance.from(6));
        Line 부산일호선 = Line.of("부산1호선", "bg-orange-600", 동래역, 서면역, Distance.from(7));
        이호선.addSection(Section.of(이호선, 당산역, 영등포구청역, Distance.from(5)));
        List<Line> lines = Arrays.asList(육호선, 이호선, 오호선, 부산일호선);
        sections = lines.stream()
                .map(Line::getSections)
                .collect(Collectors.toList());

        pathFinder = new PathFinder();
        pathFinder.decideShortestPathStrategy(new DijkstraShortestPathStrategy());
    }

    @Test
    @DisplayName("지하철 출발역과 도착역 사이의 최단 경로를 탐색한다.")
    void findShortestPath() {
        //when
        PathResponse pathResponse = pathFinder.findShortestPath(sections, 합정역, 영등포구청역);
        List<StationResponse> stations = pathResponse.getStations();
        List<String> stationNames = stations.stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        //then
        assertThat(stationNames).containsExactly("합정역", "당산역", "영등포구청역");
        assertThat(pathResponse.getDistance()).isEqualTo(8);
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우, 최단 경로를 구할 수 없다.")
    void findShortestPathBetweenSameStations() {
        assertThatThrownBy(() -> {
            PathResponse pathResponse = pathFinder.findShortestPath(sections, 합정역, 합정역);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우, 최단 경로를 구할 수 없다.")
    void findShortestPathBetweenNotConnectedStations() {
        assertThatThrownBy(() -> {
            PathResponse pathResponse = pathFinder.findShortestPath(sections, 합정역, 동래역);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 출발역이나 도착역인 경우, 최단 경로를 구할 수 없다.")
    void findShortestPathBetweenNotExistingStations() {
        assertThatThrownBy(() -> {
            Station 존재하지_않는_역 = new Station("LA");
            PathResponse pathResponse = pathFinder.findShortestPath(sections, 합정역, 존재하지_않는_역);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
