package nextstep.subway.path.utils;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.exception.IllegalPathException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JGraphTPathFinderTest {
    List<Line> lines = new ArrayList();
    Station 강남역;
    Station 교대역;
    Station 양재역;
    Station 남부터미널역;

    @BeforeEach
    void setUp() {
        강남역 = new Station(1L, "강남역");
        교대역 = new Station(2L, "교대역");
        양재역 = new Station(3L, "양재역");
        남부터미널역 = new Station(4L, "남부터미널역");

        Line 신분당선 = new Line("신분당선", "red lighten-1", 강남역, 양재역, 10);
        Line 이호선 = new Line("2호선", "green lighten-1", 교대역, 강남역, 10);
        Line 삼호선 = new Line("3호선", "orange darken-1", 교대역, 양재역, 5);
        삼호선.addSection(new Section(교대역, 남부터미널역, 3));

        lines.add(신분당선);
        lines.add(이호선);
        lines.add(삼호선);
    }

    @DisplayName("최단거리 순서대로 역을 리턴한다1")
    @Test
    void findPath() {
        JGraphTPathFinder jGraphTPathFinder = new JGraphTPathFinder();
        Path path = jGraphTPathFinder.findPath(lines, 강남역.getId(), 남부터미널역.getId());
        assertThat(path.getDistance()).isEqualTo(12);
        assertThat(path.getStationResponses().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList()))
                .containsExactlyElementsOf(Arrays.asList("강남역", "양재역", "남부터미널역"));
    }

    @DisplayName("최단거리 순서대로 역을 리턴한다2")
    @Test
    void findPath2() {
        JGraphTPathFinder jGraphTPathFinder = new JGraphTPathFinder();
        Path path = jGraphTPathFinder.findPath(lines, 교대역.getId(), 양재역.getId());
        assertThat(path.getDistance()).isEqualTo(5);
        assertThat(path.getStationResponses().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList()))
                .containsExactlyElementsOf(Arrays.asList("교대역", "남부터미널역", "양재역"));
    }

    @DisplayName("출발역과 도착역이 같을 경우 예외를 출력한다")
    @Test
    void sameSourceTargetTest() {
        JGraphTPathFinder jGraphTPathFinder = new JGraphTPathFinder();
        assertThatThrownBy(() -> {
            jGraphTPathFinder.findPath(lines, 교대역.getId(), 교대역.getId());
        }).isInstanceOf(IllegalPathException.class)
                .hasMessageContaining("출발역과 도착역이 같습니다");
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 예외를 출력한다")
    @Test
    void notConnectedTest() {
        Station 센텀시티역 = new Station("센텀시티역");

        JGraphTPathFinder jGraphTPathFinder = new JGraphTPathFinder();
        assertThatThrownBy(() -> {
            jGraphTPathFinder.findPath(lines, 교대역.getId(), 센텀시티역.getId());
        }).isInstanceOf(IllegalPathException.class)
                .hasMessageContaining("경로를 찾을 수 없습니다");
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회할 때 예외를 선택한다")
    @Test
    void noStationTest() {
        JGraphTPathFinder jGraphTPathFinder = new JGraphTPathFinder();
        assertThatThrownBy(() -> {
            jGraphTPathFinder.findPath(lines, 교대역.getId(), 10L);
        }).isInstanceOf(IllegalPathException.class)
                .hasMessageContaining("경로를 찾을 수 없습니다");
    }
}
