package nextstep.subway.path.domain;

import nextstep.subway.common.exception.CyclePathException;
import nextstep.subway.common.exception.NotFoundEntityException;
import nextstep.subway.common.exception.UnconnectedStationException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("경로 조회 도메인 관련")
class PathFinderTest {
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 사호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 이촌역;
    private Station 삼각지역;
    private Station 새로운역1;
    private List<Section> 구간들;

    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        이촌역 = new Station("이촌역");
        삼각지역 = new Station("삼각지역");
        새로운역1 = new Station("새로운역1");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);
        사호선 = new Line("사호선", "bg-red-600", 이촌역, 삼각지역, 5);

        삼호선.addLineStation(교대역, 남부터미널역, 3);
        구간들 = Arrays.asList(
                new Section(신분당선, 강남역, 양재역, 10),
                new Section(이호선, 교대역, 강남역, 10),
                new Section(삼호선, 교대역, 남부터미널역, 3),
                new Section(삼호선, 남부터미널역, 양재역, 2),
                new Section(사호선, 이촌역, 삼각지역, 5)
        );
    }

    @DisplayName("최단 경로를 찾을 수 있다.")
    @Test
    void findShortestPathSuccess() {
        PathFinder pathFinder = new PathFinder(강남역, 남부터미널역, 구간들);
        GraphPath<Station, DefaultWeightedEdge> shortestPath = pathFinder.findShortestPath();

        assertAll(
                () -> assertThat(shortestPath).isNotNull(),
                () -> assertThat(shortestPath.getVertexList()).containsExactlyElementsOf(
                        Arrays.asList(강남역, 양재역, 남부터미널역)),
                () -> assertThat(shortestPath.getWeight()).isEqualTo(12.0)
        );
    }

    @DisplayName("출발역과 종착역이 같은 경우 최단 경로를 찾을 수 없다.")
    @Test
    void findShortestPathExceptionSameStation() {
        assertThatThrownBy(() -> {
            PathFinder pathFinder = new PathFinder(강남역, 강남역, 구간들);

        }).isInstanceOf(CyclePathException.class)
        .hasMessageContaining("출발역과 종착역이 같습니다.");
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 최단 경로를 찾을 수 없다.")
    @Test
    void findShortestPathExceptionUnConnected() {
        assertThatThrownBy(() -> {
            PathFinder pathFinder = new PathFinder(강남역, 이촌역, 구간들);
            pathFinder.findShortestPath();

        }).isInstanceOf(UnconnectedStationException.class)
                .hasMessageContaining("출발역과 도착역이 연결되어 있지 않습니다.");
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우 최단 경로를 찾을 수 없다.")
    @Test
    void findShortestPathExceptionNotFoundStation() {
        assertThatThrownBy(() -> {
            PathFinder pathFinder = new PathFinder(강남역, 새로운역1, 구간들);
            pathFinder.findShortestPath();

        }).isInstanceOf(NotFoundEntityException.class)
                .hasMessageContaining("Entity를 찾지 못했습니다.");
    }
}