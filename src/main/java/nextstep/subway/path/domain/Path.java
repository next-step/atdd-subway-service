package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class Path {
    private final WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

    public GraphPath<Long, DefaultWeightedEdge> find(Station sourceStation, Station targetStation) {
        validationSameStation(sourceStation, targetStation);

        GraphPath<Long, DefaultWeightedEdge> path = findPath(sourceStation, targetStation);
        validationPathNull(path);

        return path;
    }

    private void validationPathNull(GraphPath<Long, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new RuntimeException("역 사이에 경로가 존재하지 않습니다.");
        }
    }

    private void validationSameStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new RuntimeException("출발역과 도착역과 같습니다.");
        }
    }

    public void initLines(List<Line> lines) {
        lines.stream()
                .map(Line::getSections)
                .map(Sections::getSections)
                .flatMap(Collection::stream)
                .forEach(this::setVertexAndEdgeWeight);
    }

    private void setVertexAndEdgeWeight(Section it) {
        graph.addVertex(it.getUpStation().getId());
        graph.addVertex(it.getDownStation().getId());
        graph.setEdgeWeight(graph.addEdge(it.getUpStation().getId(), it.getDownStation().getId()), it.getDistance());
    }

    private GraphPath<Long, DefaultWeightedEdge> findPath(Station sourceStation, Station targetStation) {
        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(sourceStation.getId(), targetStation.getId());
    }

}
