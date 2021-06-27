package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {

    private WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

    public PathFinder(List<Line> lines) {
        addVertex(lines);
        addEdge(lines);
    }

    private void addVertex(List<Line> lines) {
        for (Line line : lines) {
            addVertex(line);
        }
    }

    private void addVertex(Line line) {
        for (Station station : line.stations()) {
            graph.addVertex(station);
        }
    }

    private void addEdge(List<Line> lines) {
        for (Line line : lines) {
            addEdge(line.sections());
        }
    }

    private void addEdge(Sections sections) {
        for (Section section : sections) {
            setEdgeWeight(section);
        }
    }

    private void setEdgeWeight(Section section) {
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
    }

    public List<Station> findPaths(Station source, Station target) {
        validateFindable(source, target);
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    private void validateFindable(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발지와 도착지가 동일합니다. 입력값을 확인해주세요.");
        }
    }
}
