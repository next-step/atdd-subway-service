package study.jgraph;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JgraphStationTest {
    private WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
    private Line 신분당선, 분당선, _9호선, _2호선, _3호선;
    private Station 신논현역, 언주역, 선정릉역, 강남역, 역삼역, 선릉역, 양재역, 매봉역, 도곡역, 한티역;

    @BeforeEach
    void setUp() {
        지하철역_생성();
        노선_생성();
        구간_생성();
        그래프_정점_추가();
        전체_구간_가중치_설정();
    }

    /**
     *  [신분당선]                           [분당선]
     *   |                                      |
     *   |                                     |
     * 신논현 -  (7)  - 언주  -   (18)   -  `선정릉`  ---> [9호선]
     *   |                                    |
     *  (5)                                 (4)
     *   |                                  |
     * 강남   -    (6)   -   역삼 - (1) - 선릉  ---> [2호선]
     *   |                                |
     *   |                              (9)
     *   |                              |
     *  (12)                          한티
     *   |                             |
     *   |                            (6)
     *   |                            |
     * `양재` - (1) - 매봉 - (0.8) - 도곡  ---> [3호선]
     *
     * path 1: 선정릉 -> 언주 -> 신논현 -> 강남 -> 양재 : 4개역, 42
     * path 2: 선정릉 -> 선릉 -> 한티 -> 도곡 -> 매봉 -> 양재 : 5개역, 37
     * path 3: 선정릉 -> 선릉 -> 역삼 -> 강남 -> 양재 : 4개역, 32
     */
    @Test
    @DisplayName("지하철역 최단 경로 조회")
    public void 지하철역_최단_경로_조회() {
        // Given
        Station source = 선정릉역;
        Station target = 양재역;

        // When
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath path = dijkstraShortestPath.getPath(source, target);
        List<Station> shortestPath = path.getVertexList();

        // Then
        assertAll(
            () -> assertThat(path.getLength()).isEqualTo(4),
            () -> assertThat(path.getStartVertex()).isEqualTo(선정릉역),
            () -> assertThat(path.getEndVertex()).isEqualTo(양재역),
            () -> assertThat(path.getWeight()).isEqualTo(32),
            () -> assertThat(shortestPath)
                .hasSize(5)
                .containsExactly(선정릉역, 선릉역, 역삼역, 강남역, 양재역)
        );
    }

    public void 지하철역_생성() {
        신논현역 = new Station("신논현역");
        언주역 = new Station("언주역");
        선정릉역 = new Station("선정릉역");

        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        선릉역 = new Station("선릉역");

        양재역 = new Station("양재역");
        매봉역 = new Station("매봉역");
        도곡역 = new Station("도곡역");

        한티역 = new Station("한티역");
    }

    public void 노선_생성() {
        _9호선 = new Line("9호선", "brown", 신논현역, 선정릉역, 25);
        _2호선 = new Line("2호선", "green", 강남역, 선릉역, 16);
        _3호선 = new Line("3호선", "orange", 양재역, 도곡역, 18);
        분당선 = new Line("분당선", "yellow", 선정릉역, 도곡역, 19);
        신분당선 = new Line("신분당선", "red", 신논현역, 양재역, 17);
    }

    public void 구간_생성() {
        구간_추가(_9호선, 신논현역, 언주역, 7);
        구간_추가(_2호선, 강남역, 역삼역, 6);
        구간_추가(_3호선, 양재역, 매봉역, 10);
        구간_추가(신분당선, 신논현역, 강남역, 5);
        구간_추가(분당선, 선정릉역, 선릉역, 4);
        구간_추가(분당선, 선릉역, 한티역, 9);
    }

    private void 구간_추가(final Line 노선, final Station 상행역, final Station 하행역, final int 구간_거리) {
        노선.addSection(Section.of(노선, 상행역, 하행역, 구간_거리));
    }

    private void 그래프_정점_추가() {
        신분당선.getAllStations().forEach(it -> graph.addVertex(it));
        분당선.getAllStations().forEach(it -> graph.addVertex(it));
        _9호선.getAllStations().forEach(it -> graph.addVertex(it));
        _2호선.getAllStations().forEach(it -> graph.addVertex(it));
        _3호선.getAllStations().forEach(it -> graph.addVertex(it));
    }

    private void 전체_구간_가중치_설정() {
        그래프_정점간_가중치_설정(_9호선);
        그래프_정점간_가중치_설정(_2호선);
        그래프_정점간_가중치_설정(_3호선);
        그래프_정점간_가중치_설정(분당선);
        그래프_정점간_가중치_설정(신분당선);
    }

    private void 그래프_정점간_가중치_설정(Line 노선) {
        노선.getSections().forEach(it ->
            graph.setEdgeWeight(
                graph.addEdge(it.getUpStation(), it.getDownStation()),
                it.getDistance()
            ));
    }
}
