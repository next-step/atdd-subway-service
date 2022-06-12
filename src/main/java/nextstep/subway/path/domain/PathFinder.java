package nextstep.subway.path.domain;

import nextstep.subway.exception.InvalidPathException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath;

    public PathFinder(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Line line : lines) {
            Sections sections = line.getSections();
            for (Station station : sections.getStations()) {
                graph.addVertex(station);
            }
        }

        for (Line line : lines) {
            Sections sections = line.getSections();
            for (Section section : sections.getList()) {
                graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().getDistance());
            }
        }
        shortestPath = new DijkstraShortestPath<>(graph);
    }

    public List<Station> findShortestStationList(Station source, Station destination) {
        return findShortestPath(source, destination).getVertexList();
    }

    public double findShortestPathLength(Station source, Station destination) {
        return findShortestPath(source, destination).getWeight();
    }

    private GraphPath<Station, DefaultWeightedEdge> findShortestPath(Station source, Station destination) {
        validatePaths(source, destination);
        GraphPath<Station, DefaultWeightedEdge> path = shortestPath.getPath(source, destination);
        if (path == null) {
            throw new InvalidPathException("존재하지 않는 경로입니다.");
        }
        return path;
    }

    private void validatePaths(Station source, Station destination) {
        if (source.equals(destination)) {
            throw new InvalidPathException("출발역과 도착역은 달라야 합니다.");
        }
    }
}
