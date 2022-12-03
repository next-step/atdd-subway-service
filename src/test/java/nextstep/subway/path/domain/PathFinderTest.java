package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.ui.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static nextstep.subway.Fixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathFinderTest {

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 사호선;
    private Station 명동역;
    private Station 사당역;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */

    @BeforeEach
    public void setUp() {
        강남역 = createStation("강남역", 1L);
        양재역 = createStation("양재역", 2L);
        교대역 = createStation("교대역", 3L);
        남부터미널역 = createStation("남부터미널역", 4L);
        명동역 = createStation("명동역", 5L);
        사당역 = createStation("사당역", 6L);
        신분당선 = createLine("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = createLine("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = createLine("삼호선", "bg-red-600", 교대역, 양재역, 10);
        사호선 = createLine("사호선", "bg-red-600", 명동역, 사당역, 30);
        삼호선.addSection(createSection(교대역, 남부터미널역, 8));
    }

    @DisplayName("최단 경로 조회에 성공한다.")
    @Test
    void getShortestPath() {
        PathFinder pathFinder = PathFinder.from(Arrays.asList(신분당선, 이호선, 삼호선));

        PathResponse shortestPath = pathFinder.getShortestPath(강남역, 남부터미널역);

        assertThat(shortestPath.getStations().stream().map(StationResponse::getId))
                .containsExactly(강남역.getId(), 양재역.getId(), 남부터미널역.getId());
        assertThat(shortestPath.getDistance()).isEqualTo(12);
    }

    @DisplayName("10km 이내 경로 조회 시 기본운임 1,250원 요금 정보가 포함된다")
    @Test
    void getShortestPath_basicFare() {
        PathFinder pathFinder = PathFinder.from(Arrays.asList(신분당선, 이호선, 삼호선));

        PathResponse shortestPath = pathFinder.getShortestPath(강남역, 양재역);

        assertThat(shortestPath.getDistance()).isEqualTo(10);
        assertThat(shortestPath.getAdditionalFare()).isEqualTo(1250);
    }

    @DisplayName("10km 초과 ∼ 50km 이내 경로 조회 시 5km마다 100원 추가된 요금 정보가 포함된다")
    @Test
    void getShortestPath_additionalFare_level1() {
        PathFinder pathFinder = PathFinder.from(Arrays.asList(신분당선, 이호선, 삼호선));

        PathResponse shortestPath = pathFinder.getShortestPath(강남역, 남부터미널역);

        assertThat(shortestPath.getDistance()).isEqualTo(12);
        assertThat(shortestPath.getAdditionalFare()).isEqualTo(1350);
    }

    @DisplayName("50km 초과 경로 조회 시 8km마다 100원 추가된 요금 정보가 포함된다")
    @Test
    void getShortestPath_additionalFare_level2() {
        PathFinder pathFinder = PathFinder.from(Collections.singletonList(사호선));

        PathResponse shortestPath = pathFinder.getShortestPath(명동역, 사당역);

        assertThat(shortestPath.getDistance()).isEqualTo(30);
        assertThat(shortestPath.getAdditionalFare()).isEqualTo(1650);
    }

    @DisplayName("최단 경로를 조회 시, 출발역과 도착역이 같으면 예외를 반환한다.")
    @Test
    void getLinesWithException() {
        PathFinder pathFinder = PathFinder.from(Arrays.asList(신분당선, 이호선, 삼호선));

        assertThatThrownBy(() -> pathFinder.getShortestPath(강남역, 강남역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출발역과 도착역이 " + 강남역.getName() + "으로 동일합니다.");
    }

    @DisplayName("최단 경로를 조회 시, 출발역과 도착역이 연결이 되어 있지 않은 경우 예외를 반환한다.")
    @Test
    void getLinesWithException2() {
        PathFinder pathFinder = PathFinder.from(Arrays.asList(신분당선, 이호선, 삼호선, 사호선));

        assertThatThrownBy(() -> pathFinder.getShortestPath(강남역, 사당역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출발역과 도착역이 연결이 되어 있지 않습니다.");
    }
}
