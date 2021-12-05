package nextstep.subway.path.domain;

import static nextstep.subway.exception.ExceptionMessage.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jgrapht.GraphPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

class PathFinderTest {

    private PathFinder pathFinder;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("잠실역");
        양재역 = new Station("선릉역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("당산역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-green-600", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-orange-600", 교대역, 양재역, 5);
        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, 3));

        Set<Section> sections = Stream.of(신분당선, 이호선, 삼호선)
            .flatMap(line -> line.getSections().getSections().stream())
            .collect(Collectors.toSet());

        pathFinder = new PathFinder(sections);
    }

    @DisplayName("최단 경로를 조회한다.")
    @Test
    void findShortestTest() {
        // when
        GraphPath<Station, Section> path = pathFinder.getShortestPaths(강남역, 남부터미널역);

        // then
        List<Station> stations = path.getVertexList();
        assertThat(stations).containsExactlyElementsOf(Arrays.asList(강남역, 양재역, 남부터미널역));
        assertEquals(12, path.getWeight());
    }

    @DisplayName("출발역과 도착역이 같은 경우 경로를 조회할 수 없다.")
    @Test
    void validateSameStations() {
        // when && then
        assertThatThrownBy(() -> pathFinder.getShortestPaths(강남역, 강남역))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(SAME_STATION.getMessage());
    }

    @DisplayName("존재하지 않는 출발역이나 도착역은 조회할 수 없다.")
    @Test
    void validateExistStation() {
        // given
        Station 선릉역 = new Station("선릉역");
        Station 사당역 = new Station("사당역");

        // when && then
        assertThatThrownBy(() -> pathFinder.getShortestPaths(선릉역, 사당역))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(NOT_EXIST_STATION.getMessage());
    }
}
