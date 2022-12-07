package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static nextstep.subway.Fixture.createLine;
import static nextstep.subway.Fixture.createStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ShortestPathTest {

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 광산역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 학동역;
    private Station 강남구청역;

    @BeforeEach
    public void setUp() {
        강남역 = createStation("강남역", 1l);
        양재역 = createStation("양재역", 2l);
        교대역 = createStation("교대역", 3l);
        남부터미널역 = createStation("남부터미널역", 4l);
        광산역 = createStation("광산역", 7l);
        신분당선 = createLine("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = createLine("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = createLine("삼호선", "bg-red-600", 교대역, 양재역, 5);
        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, 3));
        삼호선.addSection(new Section(삼호선, 광산역, 교대역, 5));
        학동역 = createStation("강남역", 10l);
        강남구청역 = createStation("강남역", 11l);
    }

    @DisplayName("경로값이 없으면 예외발생")
    @Test
    void returnsExceptionWithNullPath() {
        assertThatThrownBy(() -> new ShortestPath(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("경로가 존재해야 합니다");
    }

    @DisplayName("최단경로에 해당하는 역들을 조회")
    @Test
    void returnsStations() {
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선));

        ShortestPath shortestPath = pathFinder.getShortestPath(광산역, 양재역,13);

        assertThat(shortestPath.getStations())
                .containsExactly(광산역, 교대역, 남부터미널역, 양재역);
    }

    @DisplayName("최단경로에 해당하는 역들의 거리 조회")
    @Test
    void returnsDistance() {
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선));

        ShortestPath shortestPath = pathFinder.getShortestPath(광산역, 양재역,13);

        assertThat(shortestPath.getDistance()).isEqualTo(10);
    }
}

