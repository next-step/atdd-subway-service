package nextstep.subway.path.application;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

@Component
public class DijkstraPathFinder implements PathFinder {
    private static final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
            DefaultWeightedEdge.class);

    public void initGraph(Set<Line> lines) {
        lines.forEach(this::initGraph);
    }

    private void initGraph(Line line) {
        initGraph(line.sections());
    }

    private void initGraph(Sections sections) {
        initGraph(sections.sections());
    }

    private void initGraph(List<Section> sections) {
        for (Section section : sections) {
            graph.addVertex(section.upStation());
            graph.addVertex(section.downStation());
            graph.setEdgeWeight(graph.addEdge(section.upStation(), section.downStation()), section.distanceValue());
        }
    }

    public Path shortestPath(Station source, Station target) {
        List<Station> stations = shortestPathVertexList(source, target);
        int distance = shortestPathWeight(source, target);
        return Path.valueOf(stations, Distance.valueOf(distance));
    }

    private List<Station> shortestPathVertexList(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> shortestPath = getShortestPath(source, target);
        return shortestPath.getVertexList();
    }

    private int shortestPathWeight(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> shortestPath = getShortestPath(source, target);
        return (int) shortestPath.getWeight();
    }

    private GraphPath<Station, DefaultWeightedEdge> getShortestPath(Station source, Station target) {
        validateNotSameStation(source, target);
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> shortestPath = dijkstraShortestPath.getPath(source, target);
        validatePathNotNull(shortestPath);
        return shortestPath;
    }

    private void validatePathNotNull(GraphPath<Station, DefaultWeightedEdge> path) {
        if (Objects.isNull(path)) {
            throw new NotFoundException("최단경로를 조회할 수 없습니다.");
        }
    }

    private void validateNotSameStation(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역이 같을 수 없습니다.");
        }
    }
}
