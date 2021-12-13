package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathDtos;
import nextstep.subway.station.domain.Station;

class StationGraphTest {
    private Line 신분당선;

    private Station 강남역;
    private Station 양재역;
    private Station 판교역;

    private Section 강남_양재_구간;
    private Section 양재_판교_구간;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "red");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        판교역 = new Station("판교역");

        강남_양재_구간 = new Section(신분당선, 강남역, 양재역, 8);
        양재_판교_구간 = new Section(신분당선, 양재역, 판교역, 2);
    }

    @DisplayName("graph 생성 후 path 확인")
    @Test
    void getDijkstraShortestPath() {
        List<Section> sections = Arrays.asList(강남_양재_구간, 양재_판교_구간);
        PathDtos paths = PathDtos.from(sections);
        StationGraph graph = new StationGraph(paths);

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = graph.getDijkstraShortestPath();

        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(강남역, 판교역);
        assertAll(
            () -> assertThat(path.getVertexList()).isEqualTo(Arrays.asList(강남역, 양재역, 판교역)),
            () -> assertThat(path.getWeight()).isEqualTo(10)
        );
    }
}