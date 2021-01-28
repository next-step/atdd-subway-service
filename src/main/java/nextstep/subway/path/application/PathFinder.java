package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathFindResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PathFinder {

    public static PathFindResponse findPath(List<Line> lines, Station source, Station target) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath<>(toGrapth(lines));
        List<Long> shortestPath = dijkstraShortestPath.getPath(source.getId(), target.getId()).getVertexList();
        return new PathFindResponse(shortestPath);
    }

    private static WeightedMultigraph<Long, DefaultWeightedEdge> toGrapth(List<Line> lines) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        lines.forEach(line -> {
            line.getStations().forEach(item -> graph.addVertex(item.getId()));
            line.getSections().getSections().forEach(section -> {
                graph.setEdgeWeight(graph.addEdge(section.getUpStation().getId(), section.getDownStation().getId()), section.getDistance());
            });
        });
        return graph;
    }

}
