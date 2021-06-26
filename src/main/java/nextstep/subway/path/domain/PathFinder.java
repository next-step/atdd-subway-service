package nextstep.subway.path.domain;

import nextstep.subway.exception.Message;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Set;

public class PathFinder {

    private DijkstraShortestPath<Station, SectionEdge> algorithm;
    private WeightedGraph<Station, SectionEdge> graph;

    public PathFinder(List<Line> lines) {
        graph = generateGraph(lines);
        this.algorithm = new DijkstraShortestPath<>(graph);
    }

    private WeightedGraph<Station, SectionEdge> generateGraph(List<Line> lines) {
        WeightedGraph<Station, SectionEdge> newGraph = new WeightedMultigraph<>(SectionEdge.class);

        addVertex(lines, newGraph);
        addEdge(lines, newGraph);

        return newGraph;
    }

    private void addEdge(List<Line> lines, WeightedGraph<Station, SectionEdge> graph) {
        lines.forEach(line -> line.getSections().getSections()
                .forEach(section -> setEdgeWithWeight(section, graph)));
    }

    private void setEdgeWithWeight(Section section, WeightedGraph<Station, SectionEdge> graph) {
        SectionEdge sectionEdge = graph.addEdge(section.getUpStation(), section.getDownStation());
        graph.setEdgeWeight(sectionEdge, section.getDistance().getDistanceValue());
    }

    private void addVertex(List<Line> lines, WeightedGraph<Station, SectionEdge> graph) {
        lines.forEach(line -> line.getSortedStation()
                .forEach(graph::addVertex));
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
