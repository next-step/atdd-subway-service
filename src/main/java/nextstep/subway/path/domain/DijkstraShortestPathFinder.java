package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class DijkstraShortestPathFinder implements ShortestPathFinder {
    private final DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath;

    public DijkstraShortestPathFinder(List<Line> lines) {
        WeightedMultigraph<Station, SectionEdge> graph = generateGraph(lines);
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    private WeightedMultigraph<Station, SectionEdge> generateGraph(List<Line> lines) {
        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
        for (Line line : lines) {
            addVertexes(graph, line);
            addEdges(graph, line);
        }
        return graph;
    }

    private void addVertexes(WeightedMultigraph<Station, SectionEdge> graph, Line line) {
        line.getStations()
                .forEach(graph::addVertex);
    }

    private void addEdges(WeightedMultigraph<Station, SectionEdge> graph, Line line) {
        line.getSections()
                .forEach(section -> {
                    SectionEdge sectionEdge = new SectionEdge(section);
                    graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, section.getDistance());
                });
    }

    @Override
    public Path getPath(Station source, Station target) {
        GraphPath<Station, SectionEdge> graphPath = dijkstraShortestPath.getPath(source, target);
        int lineOverFare = graphPath.getEdgeList().stream()
                .map(SectionEdge::getLineOverFare)
                .max(Integer::compareTo)
                .orElse(0);
        return Path.of(graphPath.getVertexList(), graphPath.getWeight(), lineOverFare);
    }
}
