package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PathFinderTest {
    private Station 강남역;
    private Station 판교역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 수원역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Lines lines;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        판교역 = new Station("판교역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        수원역 = new Station("수원역");
        신분당선 = Line.builder()
                .name("신분당선")
                .color("red")
                .upStation(강남역)
                .downStation(판교역)
                .distance(5)
                .build();
        이호선 = Line.builder()
                .name("이호선")
                .color("green")
                .upStation(교대역)
                .downStation(강남역)
                .distance(7)
                .build();
        삼호선 = Line.builder()
                .name("삼호선")
                .color("yellow")
                .upStation(남부터미널역)
                .downStation(교대역)
                .distance(12)
                .build();
        lines = new Lines(Arrays.asList(신분당선, 이호선, 삼호선));
    }

    @DisplayName("남부터미널역 -> 교대역 -> 강남역 -> 판교역 (12 + 7 + 5)")
    @Test
    void findShortestPathSuccess() {
        Path path = PathFinder.findShortestPath(lines, 남부터미널역, 판교역);
        assertThat(path.getDistance().value()).isEqualTo(24);
    }

    @DisplayName("지하철 노선 정보가 없으면 예외가 발생한다")
    @Test
    void findShortestPathFail01() {
        assertThatThrownBy(() -> PathFinder.findShortestPath(new Lines(Collections.emptyList()), 남부터미널역, 판교역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("출발역과 도착역이 중복될 수 없습니다")
    @Test
    void findShortestPathFail02() {
        assertThatThrownBy(() -> PathFinder.findShortestPath(lines, 강남역, 강남역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("경로가 존재하지 않습니다")
    @Test
    void findShortestPathFail03() {
        assertThatThrownBy(() -> PathFinder.findShortestPath(lines, 강남역, 수원역))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
