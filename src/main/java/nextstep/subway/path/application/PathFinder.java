package nextstep.subway.path.application;

import java.util.List;
import java.util.Optional;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.exception.NoSuchPathException;
import nextstep.subway.station.domain.Station;

public class PathFinder {

    private final Graph<Station, DefaultEdge> graph;

    public PathFinder(List<Line> lines) {
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        lines.stream()
            .map(Line::getSections)
            .forEach(s -> s.forEach(this::addEdgesWithVertices));
    }

    private void addEdgesWithVertices(Section section) {
        Graphs.addEdgeWithVertices(
            graph, section.getUpStation(), section.getDownStation(), section.getDistance());
    }

    public Path findPath(Station source, Station target) {
        DijkstraShortestPath<Station, DefaultEdge> dijkstraPath = new DijkstraShortestPath<>(graph);
        Optional<GraphPath<Station, DefaultEdge>> maybePath
            = Optional.ofNullable(dijkstraPath.getPaths(source).getPath(target));

        return Path.of(maybePath
            .orElseThrow(() -> new NoSuchPathException("두 역이 연결되어 있지 않습니다.")));
    }
}
