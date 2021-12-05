package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.line.domain.LineTest.신분당선;
import static nextstep.subway.station.domain.StationTest.*;
import static org.assertj.core.api.Assertions.assertThat;

class PathFinderTest {

    private Sections sections = new Sections();

    @BeforeEach
    void beforeEach() {
        sections.add(new Section(신분당선, 역삼역, 양재역, 10));
        sections.add(new Section(신분당선, 양재역, 사당역, 5));
    }

    @DisplayName("경로 찾기")
    @Test
    void enrollPathsTest(){
        PathFinder pathFinder = new PathFinder().enrollPaths(sections);
        GraphPath<Station, DefaultWeightedEdge> paths = pathFinder.findPaths(역삼역, 사당역);
        List<Station> stations = paths.getVertexList();

        assertThat(stations).hasSize(3);
        assertThat(stations).containsExactly(역삼역, 양재역, 사당역);
    }

}