package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

public class SubwayPath {

    private SubwayPath() {
    }

    public static List<Station> finder(Station source, Station target, List<Station> stations, Sections sections) {
        SubwayGraph subwayGraph = new SubwayGraph(DefaultWeightedEdge.class);
        subwayGraph.addVertex(stations);
        subwayGraph.setEdgeWeight(sections);

        return new DijkstraShortestPath(subwayGraph)
                .getPath(source, target)
                .getVertexList();
    }
}
