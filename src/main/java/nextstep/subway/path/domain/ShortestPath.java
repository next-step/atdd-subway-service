package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.List;

public class ShortestPath {

    private GraphPath<Station, SectionEdge> graph;

    private ShortestPath(GraphPath<Station, SectionEdge> graph) {
        this.graph = graph;
    }


    public static ShortestPath of(GraphPath<Station, SectionEdge> graph) {
        return new ShortestPath(graph);
    }

    public int getPathDistance() {
        return graph.getEdgeList().stream()
            .map(SectionEdge::getDistance)
            .reduce(Integer::sum)
            .orElse(0);
    }

    public int getMaxLineFare() {
        return graph.getEdgeList().stream()
            .map(SectionEdge::getLineFare)
            .reduce(Integer::max)
            .orElse(0);
    }

    public List<Station> getPath() {
        return graph.getVertexList();
    }
}
