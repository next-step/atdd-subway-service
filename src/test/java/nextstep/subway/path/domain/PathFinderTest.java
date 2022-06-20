package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static nextstep.subway.path.acceptance.PathAcceptanceMethod.getStationIds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class PathFinderTest {
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    @BeforeEach
    public void setUp() {
        강남역 = Station.from("강남역");
        양재역 = Station.from("양재역");
        교대역 = Station.from("교대역");
        남부터미널역 = Station.from("남부터미널역");

        신분당선 = new Line.Builder("신분당선", "bg-red-600")
                .section(Section.of(강남역, 양재역, 10))
                .build();
        이호선 = new Line.Builder("이호선", "bg-green-600")
                .section(Section.of(교대역, 강남역, 10))
                .build();
        삼호선 = new Line.Builder("삼호선", "bg-orange-600")
                .section(Section.of(교대역, 양재역, 5))
                .build();

        Section section = Section.of(교대역, 남부터미널역, 3);
        삼호선.addSection(section);
    }

    /**
     * 교대역    --- *2호선*(10) ---  강남역
     * |                            |
     * *3호선*(3)                   *신분당선*(10)
     * |                            |
     * 남부터미널역 --- *3호선*(2) --- 양재역
     */
    @DisplayName("출발역부터 도착역까지의 최단 경로 조회")
    @Test
    void findShortestPath() {
        // given
        PathFinder pathFinder = new DijkstraShortestPathFinder(Arrays.asList(신분당선, 이호선, 삼호선));

        // when
        Path shortestPath = pathFinder.findShortestPath(남부터미널역, 강남역);
        PathResponse pathResponse = PathResponse.from(shortestPath);

        // then
        assertAll(
                () -> assertThat(pathResponse.getDistance()).isEqualTo(12),
                () -> assertThat(getStationIds(pathResponse.getStations()))
                        .containsExactly(남부터미널역.getId(), 양재역.getId(), 강남역.getId())
        );
    }

    @DisplayName("출발역과 도착역이 같은 경우 예외 처리")
    @Test
    void equals_source_target() {
        // given
        PathFinder pathFinder = new DijkstraShortestPathFinder(Arrays.asList(신분당선, 이호선, 삼호선));

        // when & then
        assertThatThrownBy(() -> pathFinder.findShortestPath(양재역, 양재역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우 예외 처리")
    @Test
    void no_connect_source_target() {
        // given
        Station 뚝섬유원지역 = Station.from("뚝섬유원지역");
        Station 건대입구역 = Station.from("건대입구역");
        Line 칠호선 = new Line.Builder("7호선", "bg-olive-600")
                .section(Section.of(뚝섬유원지역, 건대입구역, 8))
                .build();
        PathFinder pathFinder = new DijkstraShortestPathFinder(Arrays.asList(신분당선, 이호선, 삼호선, 칠호선));

        // when & then
        assertThatThrownBy(() -> pathFinder.findShortestPath(건대입구역, 강남역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("출발역이 존재하지 않는 경우 예외 처리")
    @Test
    void not_exist_source() {
        // given
        Station 동대구역 = Station.from("동대구역");
        PathFinder pathFinder = new DijkstraShortestPathFinder(Arrays.asList(신분당선, 이호선, 삼호선));

        // when & then
        assertThatThrownBy(() -> pathFinder.findShortestPath(동대구역, 강남역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("도착역이 존재하지 않는 경우 예외 처리")
    @Test
    void not_exist_target() {
        // given
        Station 동대구역 = Station.from("동대구역");
        PathFinder pathFinder = new DijkstraShortestPathFinder(Arrays.asList(신분당선, 이호선, 삼호선));

        // when & then
        assertThatThrownBy(() -> pathFinder.findShortestPath(강남역, 동대구역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("지하철 노선이 존재하지 않는 경우 예외 처리")
    @Test
    void not_exist_lines() {
        // given
        PathFinder pathFinder = new DijkstraShortestPathFinder(Collections.emptyList());

        // when & then
        assertThatThrownBy(() -> pathFinder.findShortestPath(교대역, 양재역))
                .isInstanceOf(IllegalArgumentException.class);
    }
}