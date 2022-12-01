package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public PathFinder() {
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    }

    public Path find(Sections sections, Station sourceStation, Station targetStation) {
        fillVerticesAndEdges(graph, sections);
        return findShortestPath(graph, sourceStation, targetStation);
    }

    private void fillVerticesAndEdges(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Sections sections) {
        for (Section section : sections.values()) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    private Path findShortestPath(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Station sourceStation, Station targetStation) {
        GraphPath path = new DijkstraShortestPath(graph).getPath(sourceStation, targetStation);
        List<Station> stationPaths = path.getVertexList();
        Integer distance = (int) path.getWeight();

        return Path.of(stationPaths, distance);
    }
}
