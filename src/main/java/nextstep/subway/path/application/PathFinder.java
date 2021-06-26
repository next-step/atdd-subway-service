package nextstep.subway.path.application;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.exception.NoSuchPathException;
import nextstep.subway.station.domain.Station;

public class PathFinder {

    private final WeightedMultigraph<Station, Section> graph;

    public PathFinder(List<Line> lines) {
        this.graph = new WeightedMultigraph<>(Section.class);
        
        lines.stream()
            .map(Line::getStations)
            .flatMap(Collection::stream)
            .distinct()
            .forEach(this.graph::addVertex);
        lines.stream()
            .map(Line::getSections)
            .map(Sections::values)
            .flatMap(Collection::stream)
            .forEach(s -> this.graph.addEdge(s.getUpStation(), s.getDownStation(), s));
    }

    public Path findPath(Station source, Station target) {
        DijkstraShortestPath<Station, Section> dijkstraPath = new DijkstraShortestPath<>(graph);
        Optional<GraphPath<Station, Section>> maybePath
            = Optional.ofNullable(dijkstraPath.getPaths(source).getPath(target));

        return Path.of(maybePath
            .orElseThrow(() -> new NoSuchPathException("두 역이 연결되어 있지 않습니다.")));
    }
}
