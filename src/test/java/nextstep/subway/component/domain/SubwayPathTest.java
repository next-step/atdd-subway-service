package nextstep.subway.component.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayPathTest {

    private Station 강남역;
    private Station 광교역;
    private Station 교대역;
    private Line 신분당선;
    private Line 삼호선;
    private List<Line> lines;
    private SubwayGraph subwayGraph;

    @BeforeEach
    void setUp() {
        강남역 = new Station(1L, "강남역");
        광교역 = new Station(2L, "광교역");
        교대역 = new Station(3L, "교대역");
        신분당선 = new Line(1L, "신분당선", 강남역, 광교역, 10);
        삼호선 = new Line(2L, "삼호선", 교대역, 강남역, 5);
        lines = Arrays.asList(신분당선, 삼호선);
        subwayGraph = new SubwayGraph(SectionWeightedEdge.class);
        subwayGraph.addVertexesAndEdge(lines);
    }

    @Test
    void 최단_거리_경로_계산_결과_객체_생성() {
        List<SectionWeightedEdge> edges = new ArrayList<>(subwayGraph.getGraph().edgeSet());
        ArrayList<Station> stations = new ArrayList<>(subwayGraph.getGraph().vertexSet());
        SubwayPath subwayPath = new SubwayPath(edges, stations);
        assertThat(subwayPath.getStations()).containsExactly(강남역, 광교역, 교대역);
        assertThat(subwayPath.getSectionWeightedEdges().size()).isEqualTo(2);
    }

    @Test
    void 최단_거리_경로_계산_결과_객체_에서_총_구간_거리_값_구하기() {
        List<SectionWeightedEdge> edges = new ArrayList<>(subwayGraph.getGraph().edgeSet());
        ArrayList<Station> stations = new ArrayList<>(subwayGraph.getGraph().vertexSet());
        SubwayPath subwayPath = new SubwayPath(edges, stations);
        assertThat(subwayPath.calcTotalDistance()).isEqualTo(15);
    }
}