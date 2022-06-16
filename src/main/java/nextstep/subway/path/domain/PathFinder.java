package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PathFinder {

    private WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final LineRepository lineRepository;

    public PathFinder(List<Line> lines, LineRepository lineRepository) {
        this.lineRepository = lineRepository;
        enrollInGraph(lines);
    }

    private void enrollInGraph(List<Line> lines) {
        lines.forEach(line -> line.enrollIn(graph));
    }

    public GraphPath<Station, DefaultWeightedEdge> getPath(Station source, Station target) {
        isSame(source, target);
        isExistent(source, target);

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
        if (path == null) {
            throw new IllegalArgumentException("출발역과 도착역은 연결되어 있어야 합니다.");
        }

        return path;
    }

    private void isSame(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역은 달라야 합니다.");
        }
    }

    private void isExistent(Station source, Station target) {
        if (!graph.containsVertex(source) || !graph.containsVertex(target)) {
            throw new IllegalArgumentException("존재하지 않는 역입니다.");
        }
    }

    public void renew() {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        lineRepository.findAll()
                .forEach(line -> line.enrollIn(graph));
    }
}
