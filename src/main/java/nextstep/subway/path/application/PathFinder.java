package nextstep.subway.path.application;

import java.util.Collection;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;

public class PathFinder {

    private final WeightedMultigraph<Station, Section> graph;

    public PathFinder(List<Station> stations, List<Line> lines) {
        this.graph = new WeightedMultigraph<>(Section.class);
        
        stations
            .forEach(this.graph::addVertex);
        lines.stream()
            .map(Line::getSections)
            .flatMap(Collection::stream)
            .forEach(s -> this.graph.addEdge(s.getUpStation(), s.getDownStation(), s));
    }

    public Path findPath(Station source, Station target) {
        DijkstraShortestPath<Station, Section> dijkstraPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, Section> path = dijkstraPath.getPaths(source).getPath(target);
        return Path.of(path);
    }
}
