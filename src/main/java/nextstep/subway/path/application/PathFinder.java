package nextstep.subway.path.application;

import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.SectionEdge;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {
    private final WeightedMultigraph<Station, SectionEdge> graph;
    private PathFindAlgorithm algorithm;

    private PathFinder(List<Section> allSections, PathFindAlgorithm algorithm) {
        this.graph = makeGraph(allSections);
        this.algorithm = algorithm;
    }


    private WeightedMultigraph<Station, SectionEdge> makeGraph(final List<Section> allSections) {
        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
        allSections.forEach(
            section -> {
                graph.addVertex(section.getUpStation());
                graph.addVertex(section.getDownStation());
                graph.addEdge(section.getUpStation(), section.getDownStation(), SectionEdge.of(section));
            }
        );
        return graph;
    }

    public static PathFinder of(List<Section> allSections, PathFindAlgorithm algorithm) {
        return new PathFinder(allSections, algorithm);
    }

    public List<Station> find(Station departStation, Station destStation) {
        return algorithm.findShortestPathStations(graph, departStation, destStation);
    }

    public ShortestPath getShortestGraph(Station departStation, Station destStation) {
        GraphPath<Station, SectionEdge> shortestPathGraph = algorithm.getShortestPathGraph(graph, departStation, destStation);
        return ShortestPath.of(shortestPathGraph);
    }
}
