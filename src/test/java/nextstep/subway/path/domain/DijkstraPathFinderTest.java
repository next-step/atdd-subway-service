package nextstep.subway.path.domain;

import static nextstep.subway.line.domain.LineTestFixture.createLine;
import static nextstep.subway.line.domain.SectionTestFixture.createSection;
import static nextstep.subway.station.domain.StationTestFixture.createStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import nextstep.subway.common.constant.ErrorCode;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DijkstraPathFinderTest {

    private Line 칠호선;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 이수역;
    private Station 반포역;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    /**
     * 이수역 --- *7호선*[20] --- 반포역
     *
     * 교대역 --- *2호선*[10] --- 강남역
     *   |                      |
     * *3호선*[3]           *신분당선*[10]
     *   |                         |
     * 남부터미널역 --- *3호선*[2] --- 양재역
     */
    @BeforeEach
    public void setUp() {
        이수역 = createStation("이수역");
        반포역 = createStation("반포역");
        강남역 = createStation("강남역");
        양재역 = createStation("양재역");
        교대역 = createStation("교대역");
        남부터미널역 = createStation("남부터미널역");
        칠호선 = createLine("칠호선", "bg-khaki", 이수역, 반포역, 20);
        신분당선 = createLine("신분당선", "bg-red", 강남역, 양재역, 10);
        이호선 = createLine("이호선", "bg-green", 교대역, 강남역, 10);
        삼호선 = createLine("삼호선", "bg-orange", 교대역, 양재역, 5);
        createSection(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("지하철들 간 최단 거리의 경로를 조회한다.")
    @Test
    void findShortestPath() {
        // given
        DijkstraPathFinder dijkstraPathFinder = DijkstraPathFinder.createGraph(Arrays.asList(신분당선, 이호선, 삼호선));

        // when
        Path path = dijkstraPathFinder.findShortestPath(강남역, 남부터미널역);

        // then
        assertAll(
                () -> assertThat(path.getDistance()).isEqualTo(Distance.from(12)),
                () -> assertThat(path.unmodifiableStations()).containsExactly(강남역, 양재역, 남부터미널역)
        );
    }

    @DisplayName("출발역과 도착역이 같을 때 최단 거리 조회하고자 하면 오류가 발생한다.")
    @Test
    void findShortestPathThrowErrorWhenSourceStationIsEqualToTargetStation() {
        // given
        DijkstraPathFinder dijkstraPathFinder = DijkstraPathFinder.createGraph(Arrays.asList(칠호선, 신분당선, 이호선, 삼호선));

        // when & then
        assertThatThrownBy(() -> dijkstraPathFinder.findShortestPath(교대역, 교대역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.출발역과_도착역이_서로_같음.getErrorMessage());
    }

    @DisplayName("서로 연결되지 않은 지하철역간의 거리를 조회하면 오류가 발생한다.")
    @Test
    void findShortestPathThrowErrorWhenUnconnected() {
        // given
        DijkstraPathFinder dijkstraPathFinder = DijkstraPathFinder.createGraph(Arrays.asList(칠호선, 신분당선, 이호선, 삼호선));

        // when & then
        assertThatThrownBy(() -> dijkstraPathFinder.findShortestPath(이수역, 교대역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.출발역과_도착역은_연결되지_않음.getErrorMessage());
    }
}
