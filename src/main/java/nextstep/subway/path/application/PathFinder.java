package nextstep.subway.path.application;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private PathFindAlgorithm algorithm;

    private PathFinder(List<Section> allSections, PathFindAlgorithm algorithm) {
        this.graph = makeGraph(allSections);
        this.algorithm = algorithm;
    }


    private WeightedMultigraph<Station, DefaultWeightedEdge> makeGraph(final List<Section> allSections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        allSections.forEach(
            section -> {
                graph.addVertex(section.getUpStation());
                graph.addVertex(section.getDownStation());
                graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
            }
        );
        return graph;
    }

    public static PathFinder of(List<Section> allSections, PathFindAlgorithm algorithm) {
        return new PathFinder(allSections, algorithm);
    }

    public List<Station> find(Station departStation, Station destStation) {
        return algorithm.findShortestPath(graph, departStation, destStation);
    }
}
