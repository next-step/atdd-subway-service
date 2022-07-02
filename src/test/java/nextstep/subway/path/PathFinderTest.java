package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.application.PathFinder;
import nextstep.subway.sections.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathFinderTest {

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 부산도시철도;

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 부산역;
    private Station 자갈치역;
    private Station 남부터미널역;
    private List<Section> 모든구간;

    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        부산역 = new Station("부산역");
        남부터미널역 = new Station("남부터미널역");
        자갈치역 = new Station("자갈치역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);
        부산도시철도 = new Line("부산도시철도", "bg-red-600", 부산역, 자갈치역, 15);

        삼호선.updateSection(교대역, 남부터미널역, 3);

        모든구간 = Arrays.asList(신분당선, 이호선, 삼호선, 부산도시철도).stream()
            .map(line -> line.getSections())
            .flatMap(sections -> sections.stream())
            .collect(Collectors.toList());
    }


    @DisplayName("출발역과 도착역 사이의 최단 경로 조회")
    @Test
    public void findShortestPath() {
        //given
        PathFinder pathFinder = new PathFinder();
        //when
        GraphPath shortestPath = pathFinder.findShortestPath(모든구간, 강남역, 남부터미널역);
        //then
        assertThat(shortestPath.getVertexList()).containsExactly(강남역, 양재역, 남부터미널역);
        assertThat((long) shortestPath.getWeight()).isEqualTo(12);
        assertThat(pathFinder.findMaxLineFare(shortestPath).calculateFare(12)).isEqualTo(Fare.of(1350));
    }

    @DisplayName("노선에 추가 요금이 있는 경우 가격 확인")
    @Test
    public void findShortestPathFare() {
        //given
        PathFinder pathFinder = new PathFinder();
        신분당선.changeFare(300);
        삼호선.changeFare(900);
        //when
        GraphPath shortestPath = pathFinder.findShortestPath(모든구간, 강남역, 남부터미널역);
        //then
        assertThat(shortestPath.getVertexList()).containsExactly(강남역, 양재역, 남부터미널역);
        assertThat((long) shortestPath.getWeight()).isEqualTo(12);
        assertThat(pathFinder.findMaxLineFare(shortestPath).calculateFare(12)).isEqualTo(Fare.of(2250));
    }

    @DisplayName("출발역과 도착역이 같은 경우 최단 경로 조회를 실패한다.")
    @Test
    public void findShortestPathWithSameStation() {
        //given
        PathFinder pathFinder = new PathFinder();
        //when
        //then
        assertThatThrownBy(() -> pathFinder.findShortestPath(모든구간, 강남역, 강남역))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("출발역과 도착역이 동일하면 최단 경로를 조회할 수 없습니다.");
    }

    @DisplayName("출발역과 도착역이 연결되어 있지 않으면 최단 경로 조회를 실패한다.")
    @Test
    public void findShortestPathWithNotConnectedStation() {
        //given
        PathFinder pathFinder = new PathFinder();
        //when
        //then
        assertThatThrownBy(() -> pathFinder.findShortestPath(모든구간, 강남역, 부산역))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("지하철 역이 연결되어 있지 않습니다.");
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우 최단 경로 조회를 실패한다.")
    @Test
    public void findShortestPathWithNotExistStation() {
        //given
        PathFinder pathFinder = new PathFinder();
        //when
        //then
        assertThatThrownBy(() -> pathFinder.findShortestPath(모든구간, 강남역, new Station("없는역")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("지하철 역이 존재하지 않습니다.");
    }
}
