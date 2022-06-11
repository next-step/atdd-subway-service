package nextstep.subway.path.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

    public PathFinder(List<Line> lines) {
        createStationGraphFromLines(lines);
    }

    public void createStationGraphFromLines(List<Line> lines) {

        HashSet<Section> allSections = new HashSet<>();
        for (Line line : lines) {
            allSections.addAll(line.getSections());
        }

        allSections.stream().forEach(section -> {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),section.getDistance());
        });
    }

    public Path findShortest(Station source, Station target) {
        validateSourceTargetEquality(source, target);
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath path = dijkstraShortestPath.getPath(source, target);
        validateNullPath(path);
        return new Path(path.getVertexList(), (int) path.getWeight());
    }

    private void validateNullPath(GraphPath path) {
        if (path == null) {
            throw new IllegalArgumentException("경로를 검색 할 수 없습니다.");
        }
    }

    private void validateSourceTargetEquality(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역은 동일 할 수 없습니다.");
        }
    }
}
