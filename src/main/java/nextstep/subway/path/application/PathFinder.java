package nextstep.subway.path.application;

import java.util.List;
import java.util.Optional;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.SectionEdge;
import nextstep.subway.path.exception.NoSuchPathException;
import nextstep.subway.path.exception.SameEndpointException;
import nextstep.subway.station.domain.Station;

public class PathFinder {

    private final Graph<Station, SectionEdge> graph;

    public PathFinder(List<Line> lines) {
        this.graph = new WeightedMultigraph<>(SectionEdge.class);

        lines.stream()
            .map(Line::getSections)
            .forEach(s -> s.forEach(this::addEdgesWithVertices));
    }

    private void addEdgesWithVertices(Section section) {
        graph.addVertex(section.getUpStation());
        graph.addVertex(section.getDownStation());
        graph.addEdge(section.getUpStation(), section.getDownStation(), SectionEdge.of(section));
    }

    public Path findPath(Station source, Station target) {
        checkPathEndpoints(source, target);

        DijkstraShortestPath<Station, SectionEdge> dijkstraPath = new DijkstraShortestPath<>(graph);
        Optional<GraphPath<Station, SectionEdge>> maybePath
            = Optional.ofNullable(dijkstraPath.getPaths(source).getPath(target));

        return Path.of(maybePath
            .orElseThrow(() -> new NoSuchPathException("두 역이 연결되어 있지 않습니다.")));
    }

    private void checkPathEndpoints(Station source, Station target) {
        if (source.equals(target)) {
            throw new SameEndpointException("출발지와 목적지가 동일하여 경로를 탐색할 수 없습니다.");
        }
    }
}
