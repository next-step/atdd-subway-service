package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.exception.StationNotConnectException;
import nextstep.subway.path.exception.StationNotFoundException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class PathFinder {

    private WeightedMultigraph<Station, DefaultWeightedEdge> pathGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private DijkstraShortestPath shortestPath;

    public PathFinder(List<Line> lines) {
        addVertexes(lines);
        setEdgeWeights(lines);
        shortestPath = new DijkstraShortestPath(pathGraph);
    }

    private void addVertexes(List<Line> lines) {
        lines.stream()
                .flatMap(line -> line.getStations().stream())
                .collect(Collectors.toSet())
                .forEach(station -> pathGraph.addVertex(station));
    }

    private void setEdgeWeights(List<Line> lines) {
        lines.stream()
                .flatMap(line -> line.getSections().stream())
                .forEach(section -> setEdgeWeight(pathGraph, section));
    }

    private void setEdgeWeight(WeightedMultigraph<Station, DefaultWeightedEdge> pathGraph, Section section) {
        pathGraph.setEdgeWeight(pathGraph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
    }

    public List<Station> getPathStations(Station source, Station target) {
        return getGraphPath(source, target).getVertexList();
    }

    private GraphPath getGraphPath(Station source, Station target) {
        GraphPath graphPath;
        try {
            graphPath = shortestPath.getPath(source, target);
        } catch (IllegalArgumentException e) {
            throw new StationNotFoundException();
        }
        checkConnected(graphPath);
        return graphPath;
    }

    private void checkConnected(GraphPath graphPath) {
        if (graphPath == null) {
            throw new StationNotConnectException();
        }
    }

    public int getDistance(Station source, Station target) {
        return (int) shortestPath.getPath(source, target).getWeight();
    }

}
