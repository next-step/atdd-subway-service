package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class Path {
    private final WeightedMultigraph<Long, SectionEdge> graph = new WeightedMultigraph(SectionEdge.class);

    public GraphPath<Long, SectionEdge> find(Station sourceStation, Station targetStation) {
        validationSameStation(sourceStation, targetStation);

        GraphPath<Long, SectionEdge> path = findPath(sourceStation, targetStation);
        validationPathNull(path);

        return path;
    }

    private void validationPathNull(GraphPath<Long, SectionEdge> path) {
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
        SectionEdge sectionEdge = new SectionEdge(it);
        graph.addEdge(it.getUpStation().getId(), it.getDownStation().getId(), sectionEdge);
        graph.setEdgeWeight(sectionEdge, it.getDistance());
    }

    private GraphPath<Long, SectionEdge> findPath(Station sourceStation, Station targetStation) {
        DijkstraShortestPath<Long, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(sourceStation.getId(), targetStation.getId());
    }

}
