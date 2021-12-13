package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class SubwayGraphTest {
    private Station 강남역;
    private Station 교대역;
    private Station 양재역;
    private Station 남부터미널역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        교대역 = new Station("교대역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);
        삼호선.addLineSection(교대역, 남부터미널역, 3);
    }

    @DisplayName("지하철역을 포함한 노선들로 정점을 지정한다.")
    @Test
    void addVertexWithStations() {
        //when
        SubwayGraph subwayGraph = new SubwayGraph(DefaultWeightedEdge.class);
        subwayGraph.addVertexWithStations(Arrays.asList(신분당선, 이호선, 삼호선));

        //then
        assertAll(
                () -> assertThat(subwayGraph.containsVertex(강남역)).isTrue(),
                () -> assertThat(subwayGraph.containsVertex(양재역)).isTrue(),
                () -> assertThat(subwayGraph.containsVertex(교대역)).isTrue(),
                () -> assertThat(subwayGraph.containsVertex(남부터미널역)).isTrue()
        );
    }

    @DisplayName("구간을 포함한 노선들로 간선을 지정한다.")
    @Test
    void setEdgeWeightWithSections() {
        //when
        SubwayGraph subwayGraph = new SubwayGraph(DefaultWeightedEdge.class);
        subwayGraph.addVertexWithStations(Arrays.asList(신분당선, 이호선, 삼호선));
        subwayGraph.setEdgeWeightWithSections(Arrays.asList(신분당선, 이호선, 삼호선));

        //then
        assertAll(
                () -> assertThat(subwayGraph.containsEdge(강남역, 양재역)).isTrue(),
                () -> assertThat(subwayGraph.containsEdge(교대역, 강남역)).isTrue(),
                () -> assertThat(subwayGraph.containsEdge(교대역, 남부터미널역)).isTrue(),
                () -> assertThat(subwayGraph.containsEdge(교대역, 양재역)).isFalse(),
                () -> assertThat(subwayGraph.containsEdge(강남역, 남부터미널역)).isFalse()
        );
    }
}
