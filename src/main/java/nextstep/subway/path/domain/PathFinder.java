package nextstep.subway.path.domain;

import nextstep.subway.exception.Message;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Set;

public class PathFinder {

    private DijkstraShortestPath<Station, SectionEdge> algorithm;
    private WeightedGraph<Station, SectionEdge> graph;


    public PathFinder(Lines lines) {
        graph = new WeightedMultigraph<>(SectionEdge.class);
        generateGraph(lines, graph);
        this.algorithm = new DijkstraShortestPath<>(graph);
    }

    private void generateGraph(Lines lines, WeightedGraph<Station, SectionEdge> graph) {
        addVertex(lines, graph);
        addEdge(lines, graph);
    }

    private void addEdge(Lines lines, WeightedGraph<Station, SectionEdge> graph) {
        lines.getAllSections()
                .forEach(section -> setEdgeWithWeight(section, graph));
    }

    private void setEdgeWithWeight(Section section, WeightedGraph<Station, SectionEdge> graph) {
        SectionEdge sectionEdge = graph.addEdge(section.getUpStation(), section.getDownStation());
        graph.setEdgeWeight(sectionEdge, section.getDistance().getDistanceValue());
    }

    private void addVertex(Lines lines, WeightedGraph<Station, SectionEdge> graph) {
        lines.getAllStations()
                .forEach(graph::addVertex);
    }

    public Path getDijkstraShortestPath(Station startStation, Station endStation) {
        validateStations(startStation, endStation);
        return new Path(algorithm.getPath(startStation, endStation));
    }

    private void validateStations(Station startStation, Station endStation) {
        if (startStation.equals(endStation)) {
            throw new IllegalArgumentException(Message.ERROR_START_AND_END_STATIONS_ARE_SAME.showText());
        }
        checkStationsRegistered(startStation, endStation);
    }

    private void checkStationsRegistered(Station startStation, Station endStation) {
        Set<Station> stations = graph.vertexSet();
        if (!stations.contains(startStation) || !stations.contains(endStation)) {
            throw new IllegalArgumentException(Message.ERROR_START_OR_END_STATIONS_NOT_REGISTERED.showText());
        }
    }
}
