package nextstep.subway.path.domain;

import static java.lang.Integer.*;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathFinderResult;
import nextstep.subway.station.domain.Station;

public class PathFinder {

    public PathFinderResult find(List<Line> allLines, Long sourceId, Long targetId) {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = makeGraph(allLines);
        List<Long> shortestPath = getShortestStationIds(graph, sourceId, targetId);
        int minDistance = getShortestDistance(sourceId, targetId, graph);
        return new PathFinderResult(shortestPath, minDistance);
    }

    private int getShortestDistance(Long sourceId, Long targetId,
        WeightedMultigraph<String, DefaultWeightedEdge> graph) {
        return minDistance(
            new KShortestPaths(graph, MAX_VALUE).getPaths(String.valueOf(sourceId), String.valueOf(targetId)));
    }

    private List<Long> getShortestStationIds(WeightedMultigraph<String, DefaultWeightedEdge> graph, Long sourceId,
        Long targetId) {
        return toLongIds(new DijkstraShortestPath(graph).getPath(String.valueOf(sourceId),
            String.valueOf(targetId))
            .getVertexList());
    }

    private WeightedMultigraph<String, DefaultWeightedEdge> makeGraph(List<Line> allLines) {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        for (Line line : allLines) {
            addVertex(graph, line);
            setEdgeWeight(graph, line);
        }
        return graph;
    }

    private void setEdgeWeight(WeightedMultigraph<String, DefaultWeightedEdge> graph, Line line) {
        for (Section section : line.getSections()) {
            graph.setEdgeWeight(graph.addEdge(String.valueOf(section.getUpStation().getId()),
                String.valueOf(section.getDownStation().getId())),
                section.getDistance());
        }
    }

    private void addVertex(WeightedMultigraph<String, DefaultWeightedEdge> graph, Line line) {
        for (Station station : line.getStations()) {
            graph.addVertex(String.valueOf(station.getId()));
        }
    }

    private int minDistance(List<GraphPath> paths) {
        return (int)paths.stream().map(GraphPath::getWeight)
            .mapToDouble(x -> x)
            .min().orElseThrow(RuntimeException::new);
    }

    private List<Long> toLongIds(List<String> shortestPath) {
        return shortestPath.stream().map(Long::valueOf).collect(Collectors.toList());
    }
}
