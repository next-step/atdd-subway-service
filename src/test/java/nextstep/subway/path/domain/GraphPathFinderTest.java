package nextstep.subway.path.domain;

import static nextstep.subway.exception.ExceptionMessage.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

class GraphPathFinderTest {

    private PathFinder pathFinder;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Set<Section> sections;

    @BeforeEach
    void setUp() {
        강남역 = new Station("잠실역");
        양재역 = new Station("선릉역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("당산역");

        신분당선 = new Line("신분당선", "bg-red-600", 0, 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-green-600", 0, 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-orange-600", 0, 교대역, 양재역, 5);
        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, 3));

        sections = Stream.of(신분당선, 이호선, 삼호선)
            .flatMap(line -> line.getSections().getSections().stream())
            .collect(Collectors.toSet());

        pathFinder = new GraphPathFinder();
    }

    @DisplayName("최단 경로를 조회한다.")
    @Test
    void findShortestTest() {
        // when
        PathResponse findPath = pathFinder.getShortestPaths(sections, 강남역, 남부터미널역);

        // then
        assertThat(findPath.getStations()).extracting("name")
            .containsExactly(강남역.getName(), 양재역.getName(), 남부터미널역.getName());
        assertEquals(12, findPath.getDistance());
    }

    @DisplayName("출발역과 도착역이 같은 경우 경로를 조회할 수 없다.")
    @Test
    void validateSameStations() {
        // when && then
        assertThatThrownBy(() -> pathFinder.getShortestPaths(sections, 강남역, 강남역))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(SAME_STATION.getMessage());
    }

    @DisplayName("존재하지 않는 출발역이나 도착역은 조회할 수 없다.")
    @Test
    void validateExistStation() {
        // given
        Station 선릉역 = new Station("선릉역");
        Station 사당역 = new Station("사당역");

        // when && then
        assertThatThrownBy(() -> pathFinder.getShortestPaths(sections, 선릉역, 사당역))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(NOT_EXIST_STATION.getMessage());
    }
}
