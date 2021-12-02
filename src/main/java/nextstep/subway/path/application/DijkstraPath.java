package nextstep.subway.path.application;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.SectionEdge;
import nextstep.subway.path.dto.PathResult;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class DijkstraPath implements Path {

    private final DijkstraShortestPath dijkstra;

    public DijkstraPath(WeightedMultigraph<Station, SectionEdge> graph) {
        dijkstra = new DijkstraShortestPath(graph);
    }

    @Override
    public PathResult find(Station source, Station target) {
        GraphPath<Station, SectionEdge> path = dijkstra.getPath(source, target);
        List<Station> shortestPath = path.getVertexList();
        int totalDistance = (int) path.getWeight();

        return new PathResult(shortestPath, totalDistance, getFare(path));
    }

    private Fare getFare(GraphPath<Station, SectionEdge> path) {
        Sections sections = new Sections(path.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList()));

        return Fare.of(sections.getMaxLineFare());
    }
}
