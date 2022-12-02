package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class DijkstraPathFinder implements PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

    private DijkstraPathFinder(List<Section> sections) {
        sections.forEach(it -> {
            addVertex(it.getUpStation());
            addVertex(it.getDownStation());
            graph.setEdgeWeight(graph.addEdge(it.getUpStation(), it.getDownStation()), it.getDistance());
        });
    }

    public static DijkstraPathFinder from(List<Section> sections) {
        return new DijkstraPathFinder(sections);
    }

    private void addVertex(Station station) {
        if (graph.containsVertex(station)) {
            return;
        }
        graph.addVertex(station);
    }
}
