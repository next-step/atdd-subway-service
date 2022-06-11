package nextstep.subway.path.application;

import static nextstep.subway.line.domain.DomainFixtureFactory.createLine;
import static nextstep.subway.line.domain.DomainFixtureFactory.createSection;
import static nextstep.subway.line.domain.DomainFixtureFactory.createStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class DijkstraPathFinderTest {
    Station 강남역;
    Station 양재역;
    Station 교대역;
    Station 남부터미널역;
    Station 강동역;
    Station 천호역;
    Line 신분당선;
    Line 이호선;
    Line 삼호선;
    Line 오호선;

    @BeforeEach
    public void setUp() {
        강남역 = createStation(1L, "강남역");
        양재역 = createStation(2L, "양재역");
        교대역 = createStation(3L, "교대역");
        남부터미널역 = createStation(4L, "남부터미널역");
        강동역 = createStation(5L, "강동역");
        천호역 = createStation(6L, "천호역");
        신분당선 = createLine(1L, "신분당선", "bg-red-600", 강남역, 양재역, Distance.valueOf(10));
        이호선 = createLine(2L, "이호선", "bg-red-600", 교대역, 강남역, Distance.valueOf(10));
        삼호선 = createLine(3L, "삼호선", "bg-red-600", 교대역, 양재역, Distance.valueOf(5));
        오호선 = createLine(4L, "오호선", "bg-red-600", 강동역, 천호역, Distance.valueOf(5));
        삼호선.addSection(createSection(삼호선, 교대역, 남부터미널역, Distance.valueOf(3)));
    }

    @DisplayName("최단 경로 조회 테스트")
    @Test
    void shortestPath() {
        DijkstraPathFinder dijkstraPathFinder = new DijkstraPathFinder();
        dijkstraPathFinder.initGraph(new HashSet<>(Arrays.asList(신분당선, 이호선, 삼호선)));
        List<Station> path = dijkstraPathFinder.shortestPathVertexList(교대역, 양재역);
        int weight = dijkstraPathFinder.shortestPathWeight(교대역, 양재역);
        assertAll(
                () -> assertThat(path).containsExactlyElementsOf(Lists.newArrayList(교대역, 남부터미널역, 양재역)),
                () -> assertThat(weight).isEqualTo(5)
        );
    }

    @DisplayName("경로에 같은 도착역, 목적역으로 최단경로를 조회시 실패 테스트")
    @Test
    void shortestPathWithSameSourceAndTarget() {
        DijkstraPathFinder dijkstraPathFinder = new DijkstraPathFinder();
        dijkstraPathFinder.initGraph(new HashSet<>(Arrays.asList(신분당선, 이호선, 삼호선)));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> dijkstraPathFinder.shortestPathVertexList(교대역, 교대역))
                .withMessage("출발역과 도착역이 같을 수 없습니다.");
    }

    @DisplayName("경로에 출발역, 도착역이 연결이 되지 않은 상태로 조회시 실패 테스트")
    @Test
    void shortestPathWithUnConnectedSourceAndTarget() {
        DijkstraPathFinder dijkstraPathFinder = new DijkstraPathFinder();
        dijkstraPathFinder.initGraph(new HashSet<>(Arrays.asList(신분당선, 이호선, 삼호선, 오호선)));
        assertThatThrownBy(() -> dijkstraPathFinder.shortestPathVertexList(교대역, 천호역))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("최단경로를 조회할 수 없습니다.");
    }

    @DisplayName("경로에 존재하지 않는 출발역이나 도착역으로 최단경로 조회시 실패 테스트")
    @Test
    void shortestPathWithExcludeStation() {
        DijkstraPathFinder dijkstraPathFinder = new DijkstraPathFinder();
        dijkstraPathFinder.initGraph(new HashSet<>(Arrays.asList(신분당선, 이호선, 삼호선)));
        assertThatThrownBy(() -> dijkstraPathFinder.shortestPathVertexList(교대역, 강동역))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("최단경로를 조회할 수 없습니다.");
    }

    @DisplayName("경로를 initGraph 하지 않고 출발역이나 도착역으로 최단경로 조회시 실패 테스트")
    @Test
    void shortestPathWithoutInitGraph() {
        DijkstraPathFinder dijkstraPathFinder = new DijkstraPathFinder();
        assertThatThrownBy(() -> dijkstraPathFinder.shortestPathVertexList(교대역, 강동역))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("최단경로를 조회할 수 없습니다.");
    }
}
