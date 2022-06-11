package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Line.Builder;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.application.JGraphPathFindService;
import nextstep.subway.path.application.SectionEdge;
import nextstep.subway.path.domain.exception.NotExistPathException;
import nextstep.subway.station.domain.Station;
import org.assertj.core.util.Lists;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class PathFindServiceTest {

    PathFindService pathFindService = new JGraphPathFindService();

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역 = new Station("강남역");
    private Station 양재역 = new Station("양재역");
    private Station 교대역 = new Station("교대역");
    private Station 남부터미널역 = new Station("남부터미널역");
    private Station 공사중인역 = new Station("공사중인역");

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(강남역, "id", 1L);
        ReflectionTestUtils.setField(양재역, "id", 2L);
        ReflectionTestUtils.setField(교대역, "id", 3L);
        ReflectionTestUtils.setField(남부터미널역, "id", 4L);

        신분당선 = 노선생성("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 노선생성("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = 노선생성("삼호선", "bg-red-600", 교대역, 양재역, 5);
        구간추가(삼호선, 교대역, 남부터미널역, 3);
    }

    /**
     * 교대역
     * |
     * *3호선*
     * |
     *남부터미널역  --- *3호선* ---   양재
     */
    @Test
    void 그래프에_노선추가_테스트() {
        WeightedMultigraph<Station, SectionEdge> 지하철그래프 = 그래프_생성(Lists.newArrayList(삼호선));
        assertThat(지하철그래프.vertexSet()).hasSize(3);
        assertThat(지하철그래프.edgeSet()).hasSize(2);
    }

    /**
     * 교대역  --- *2호선* ---   강남역
     * |                          |
     * *3호선*                 *신분당선*
     * |                          |
     * 남부터미널역 --- *3호선* --- 양재
     */
    @Test
    void 최단경로테스트() throws Exception {
        WeightedMultigraph<Station, SectionEdge> 지하철그래프 = 그래프_생성(Lists.newArrayList(신분당선,이호선,삼호선));
        PathFindResult result = pathFindService.findShortestPath(지하철그래프, 교대역, 양재역);
        assertThat(result.getStations())
                .hasSize(3)
                .containsExactly(교대역, 남부터미널역, 양재역);
    }

    /**
     * 교대역                    강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역               양재
     */
    @Test
    void 경로가_없는경우() {
        구간제거(삼호선, 양재역);
        WeightedMultigraph<Station, SectionEdge> 지하철그래프 = 그래프_생성(Lists.newArrayList(신분당선,삼호선));
        assertThatThrownBy(() -> {
            PathFindResult result = pathFindService.findShortestPath(지하철그래프, 교대역, 양재역);
        }).isInstanceOf(NotExistPathException.class);
    }

    @Test
    void 출발역과_도착역이_같은경우() throws Exception {
        WeightedMultigraph<Station, SectionEdge> 지하철그래프 = 그래프_생성(Lists.newArrayList(신분당선,이호선, 삼호선));
        PathFindResult result = pathFindService.findShortestPath(지하철그래프, 교대역, 교대역);
        assertThat(result.getStations()).hasSize(1).containsOnly(교대역);
        assertThat(result.getDistance()).isZero();
    }

    @Test
    void 존재하지않는_목적지() throws Exception {
        WeightedMultigraph<Station, SectionEdge> 지하철그래프 = 그래프_생성(Lists.newArrayList(신분당선,이호선, 삼호선));
        assertThatThrownBy(() -> {
            PathFindResult result = pathFindService.findShortestPath(지하철그래프, 교대역, 공사중인역);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    private Line 노선생성(String name, String color, Station upStation,
                      Station downStation, int distance) {
        return new Builder(name, color)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build();
    }

    private void 구간추가(Line line, Station upStation, Station downStation, int distance) {
        line.addSection(new Section(line, upStation, downStation, distance));
    }

    private void 구간제거(Line line, Station targetStation) {
        line.deleteStation(targetStation);
    }

    private WeightedMultigraph<Station, SectionEdge> 그래프_생성(List<Line> lines) {
        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
        lines.stream().forEach(line -> 그래프에_노선정보_반영(graph, line));
        return graph;
    }

    private void 그래프에_노선정보_반영(WeightedMultigraph<Station, SectionEdge> graph, Line line) {
        List<Station> stations = line.getStations();
        stations.stream().forEach(graph::addVertex);
        Sections sections = line.getSections();
        List<SectionEdge> edges = sections.toSectionEdge();
        edges.stream().forEach(edge -> graph.addEdge(edge.getSource(), edge.getTarget(), edge));
    }
}
