package nextstep.subway.line.domain;

import nextstep.subway.exception.NotConnectStationException;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.util.ObjectUtils;

import java.util.List;

public class PathFinder {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private DijkstraShortestPath shortestPath = new DijkstraShortestPath(graph);

    protected PathFinder() {
    }

    public static PathFinder of() {
        return new PathFinder();
    }

    public PathResponse findShortest(List<Line> lines, Station source, Station target) {
        for (Line line : lines) {
            addSectionToGraph(line);
        }

        GraphPath path = shortestPath.getPath(source, target);
        if (ObjectUtils.isEmpty(path)) {
            throw new NotConnectStationException("출발지와 도착지가 연결되지 않음.");
        }

        double pathWeight = shortestPath.getPathWeight(source, target);
        return PathResponse.of(path.getVertexList(), (int) pathWeight);
    }

    private void addSectionToGraph(Line line) {
        for (Section section : line.getSections()) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistanceValue());
        }
    }
}
