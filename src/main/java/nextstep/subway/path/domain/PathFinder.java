package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {
    private final WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    public static PathFinder of(List<Line> lines) {
        return new PathFinder(lines);
    }

    private PathFinder(List<Line> lines) {
       lines.forEach(line -> makeGraph(line));
    }

    private void makeGraph(Line line) {
        for (Station station : line.getStations()) {
            graph.addVertex(station.getId());
        }

        for (Section section : line.getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStationId(), section.getDownStationId()), section.getDistance());
        }
    }

    public Path findShortestPath(Station source, Station target) {
        validatePathFinder(source, target);
        try {
            DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
            List<Long> stationIds = dijkstraShortestPath.getPath(source.getId(), target.getId()).getVertexList();
            int distance = (int) dijkstraShortestPath.getPath(source.getId(), target.getId()).getWeight();
            return new Path(stationIds, distance);
        }
        catch (RuntimeException e) {
            throw new RuntimeException("경로 탐색에 실패하였습니다.");
        }
    }

    private void validatePathFinder(Station source, Station target) {
        if (source.getId().equals(target.getId())) {
            throw new RuntimeException("동일한 두 구간에 대한 탐색은 할 수 없습니다.");
        }
    }

}
