package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collection;
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

class PathFinderTest {

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 공항철도선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 개화역;
    private Station 김포공항역;
    private PathFinder pathFinder;


    /**
     *   개화역         교대역     --- *2호선* ---     강남역
     *     |             |                            |
     * *김포공항선*     *3호선*                     *신분당선*
     *     |             |                            |
     *  김포공항역     남부터미널역  --- *3호선* ---     양재
     */

    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        개화역 = new Station("개화역");
        김포공항역 = new Station("김포공항역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = Line.of("신분당선","bg-red-600", 강남역, 양재역, Distance.from(10));
        이호선 = Line.of("이호선", "bg-green-600", 교대역, 강남역, Distance.from(10));
        삼호선 = Line.of("삼호선", "bg-orange-600", 교대역, 양재역, Distance.from(5));
        공항철도선 = Line.of("육호선", "bg-sky-600", 개화역, 김포공항역, Distance.from(5));

        삼호선.addSection(Section.of(삼호선, 교대역, 남부터미널역, Distance.from(3)));

        pathFinder = new PathFinder();
        pathFinder.determinePathFindStrategy(new JGraphPathFinder());
    }

    @Test
    @DisplayName("최단 경로를 조회한다.")
    void findShortestPath() {
        // given
        List<Section> sections = getSectionsFromLines(Arrays.asList(이호선, 삼호선, 신분당선));

        // when
        PathResponse pathResponse = pathFinder.searchShortestPath(sections, 강남역, 남부터미널역);
        List<StationResponse> stations = pathResponse.getStations();
        List<String> stationNames = stations.stream()
            .map(StationResponse::getName)
            .collect(Collectors.toList());

        // then
        assertAll(
            () -> assertThat(stationNames).containsExactly("강남역", "양재역", "남부터미널역"),
            () -> assertThat(pathResponse.getDistance()).isEqualTo(12)
        );
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우 최단 경로 조회 불가")
    void findShortestPathWithSameStations() {
        // given
        List<Section> sections = getSectionsFromLines(Arrays.asList(이호선, 삼호선, 신분당선));

        // then
        assertThatThrownBy(() -> {
            PathResponse pathResponse = pathFinder.searchShortestPath(sections, 양재역, 양재역);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 최단 경로 조회 불가")
    void findShortestPathWithoutConnection() {
        // given
        List<Section> sections = getSectionsFromLines(Arrays.asList(이호선, 삼호선, 신분당선));

        // then
        assertThatThrownBy(() -> {
            PathResponse pathResponse = pathFinder.searchShortestPath(sections, 양재역, 개화역);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우 최단 경로 조회 불가")
    void findShortestPathWithInvalidStations() {
        // given
        List<Section> sections = getSectionsFromLines(Arrays.asList(이호선, 삼호선, 신분당선));

        // when
        Station 마곡나루역 = new Station("마곡나루역");

        // then
        assertThatThrownBy(() -> {
            PathResponse pathResponse = pathFinder.searchShortestPath(sections, 양재역, 마곡나루역);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    private List<Section> getSectionsFromLines(List<Line> lines) {
        return lines.stream()
            .map(Line::getSections)
            .map(Sections::getSections)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }
}
