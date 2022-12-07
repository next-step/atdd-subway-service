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

public class PathFinderTest {

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 칠호선;
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
        칠호선 = createLine("칠호선", "bg-green-600", 학동역, 강남구청역, 10);
    }

    @DisplayName("존재하지 않는 노선을 입력하면 예외발생 ")
    @Test
    void returnsExceptionWithNullLines() {
        assertThatThrownBy(() -> new PathFinder(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("노선이 존재해야합니다");
    }

    @DisplayName("출발역과 도착역이 같으면 예외발생")
    @Test
    void returnsExceptionWithSameStations() {
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선));

        assertThatThrownBy(() -> pathFinder.getShortestPath(강남역, 강남역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출발역과 도착역 다른 경우만 조회할 수 있습니다");
    }

    @DisplayName("노선에 존재하지 않는 역을 조회하면 예외발생")
    @Test
    void returnsExceptionWithNoneExistsStartStation() {
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선));

        assertThatThrownBy(() -> pathFinder.getShortestPath(createStation("마포역", 3l), 강남역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출발역과 도착역이 노선에 존재해야합니다");
    }

    @DisplayName("출발역과 도착역이 연결되지않으면 예외발생")
    @Test
    void returnsExceptionWithNoneLinkStation() {
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선, 칠호선));

        assertThatThrownBy(() -> pathFinder.getShortestPath(학동역, 강남역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출발역과 도착역이 연결되있어야 합니다");
    }

    @DisplayName("출발역과 도착역이 연결된상태로 존재하면 최단거리 반환")
    @Test
    void returnsShortestPath() {
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선));

        assertThat(pathFinder.getShortestPath(광산역, 양재역)).isNotNull();
    }
}

