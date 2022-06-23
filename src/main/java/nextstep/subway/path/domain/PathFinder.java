package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class PathFinder {
    private final List<Line> lines;
    private final WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);

    public PathFinder(List<Line> lines) {
        this.lines = lines;
        initGraph();
    }

    private void initGraph() {
        lines.stream()
                .map(Line::getSections)
                .map(Sections::getSections)
                .flatMap(Collection::stream)
                .forEach(this::setVertexAndEdgeWeight);
    }

    private void setVertexAndEdgeWeight(Section it) {
        graph.addVertex(it.getUpStation());
        graph.addVertex(it.getDownStation());

        SectionEdge sectionEdge = new SectionEdge(it);
        graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
        graph.setEdgeWeight(sectionEdge, it.getDistance().getDistance());
    }

    public Path findPath(Station source, Station target) {
        raiseIfNotValidFindPath(source, target);

        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return new Path(dijkstraShortestPath.getPath(source, target));
    }

    private void raiseIfNotValidFindPath(Station source, Station target) {
        if (Objects.equals(source, target)) {
            throw new IllegalArgumentException("출발역과 도착역이 같으면 경로를 찾을 수 없습니다.");
        }
    }
}
