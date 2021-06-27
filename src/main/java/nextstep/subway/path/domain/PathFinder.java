package nextstep.subway.path.domain;

import java.util.List;
import java.util.Optional;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.line.exception.NotFoundException;
import nextstep.subway.station.domain.Station;

public class PathFinder {
    private final Sections sections;

    public PathFinder(final List<Section> sections) {
        this.sections = new Sections(sections);
    }

    public Path findShortestPath(final Station source, final Station target) {
        final GraphPath<Station, DefaultWeightedEdge> path = Optional.ofNullable(shortestPath(source, target))
            .orElseThrow(NotFoundException::new);

        return new Path(path.getVertexList(), (int)path.getWeight());
    }

    private GraphPath<Station, DefaultWeightedEdge> shortestPath(final Station source, final Station target) {
        validateStations(source, target);
        final SectionGraph sectionGraph = new SectionGraph(sections);

        return new DijkstraShortestPath<>(sectionGraph.graph())
            .getPath(source, target);
    }

    private void validateStations(final Station source, final Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역이 같으면 조회할 수 없습니다.");
        }
    }
}
