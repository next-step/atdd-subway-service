package nextstep.subway.path.domain;


import static nextstep.subway.line.domain.LineTestFixture.createLine;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DijkstraPathExplorerTest {
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 김포공항역;
    private Station 마곡나루역;

    private Line 이호선;
    private Line 삼호선;
    private Line 신분당선;
    private Line 공항선;

    /**
     * 김포공항    --- *공항선(30)* --- 마곡나루
     * 교대역    --- *2호선(10)* ---   강남역
     * |                                 |
     * *3호선(15)*                   *신분당선(5)*
     * |                                |
     * 남부터미널역  --- *3호선(5)* ---   양재
     */
    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        김포공항역 = new Station("김포공항역");
        마곡나루역 = new Station("마곡나루역");

        이호선 = createLine("이호선", 교대역, 강남역, 10);
        삼호선 = createLine("삼호선", 교대역, 양재역, 20);
        신분당선 = createLine("신분당선", 강남역, 양재역, 5);
        공항선 = createLine("공항선", 김포공항역, 마곡나루역, 30);

        삼호선.addSection(new Section(교대역, 남부터미널역, 15));
    }

    @DisplayName("출발역과 도착역 사이의 최단 경로를 조회할 수 있다.")
    @Test
    void findShortestPath() {
        DijkstraPathExplorer dijkstraPathExplorer = new DijkstraPathExplorer(Arrays.asList(이호선, 삼호선, 신분당선));
        Path expectedPath = new Path(Arrays.asList(남부터미널역, 양재역, 강남역), 10);

        Path actualPath = dijkstraPathExplorer.explore(남부터미널역, 강남역);

        assertThat(expectedPath).isEqualTo(actualPath);
    }

    @DisplayName("출발역과 도착역이 동일한 경우 최단 경로 조회 시 예외가 발생한다.")
    @Test
    void findShortestPathWithSameStation() {
        DijkstraPathExplorer dijkstraPathExplorer = new DijkstraPathExplorer(Arrays.asList(이호선, 삼호선, 신분당선));

        assertThatThrownBy(() -> dijkstraPathExplorer.explore(강남역, 강남역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("출발역과 도착역이 동일한 경우 경로를 조회할 수 없습니다.");
    }

    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우 최단 경로 조회 시 예외가 발생한다.")
    @Test
    void findShortestPathWithNotConnectStation() {
        DijkstraPathExplorer dijkstraPathExplorer = new DijkstraPathExplorer(Arrays.asList(이호선, 삼호선, 신분당선, 공항선));

        assertThatThrownBy(() -> dijkstraPathExplorer.explore(김포공항역, 강남역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("출발역과 도착역이 연결되어 있지 않은 경우 경로를 조회할 수 없습니다.");
    }
}
