package nextstep.subway.path.application;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.SectionEdge;
import nextstep.subway.path.dto.PathResult;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.stream.Collectors;

public class DijkstraPath implements Path {

    private final DijkstraShortestPath dijkstra;

    public DijkstraPath(WeightedMultigraph<Station, SectionEdge> graph) {
        dijkstra = new DijkstraShortestPath(graph);
    }

    @Override
    public PathResult find(Station source, Station target) {
        GraphPath<Station, SectionEdge> path = dijkstra.getPath(source, target);
        Sections sections = new Sections(path.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList()));

        return new PathResult(path.getVertexList(), path.getWeight(), sections);
    }

}
