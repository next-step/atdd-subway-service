package nextstep.subway.path.domain;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

public class PathFinder {
    private final Sections sections;

    public PathFinder(final List<Section> sections) {
        this.sections = new Sections(sections);
    }

    public Path findShortestPath(final Station source, final Station target) {
        final GraphPath<Station, DefaultWeightedEdge> path = shortestPath(source, target);

        return new Path(path.getVertexList(), (int)path.getWeight());
    }

    private GraphPath<Station, DefaultWeightedEdge> shortestPath(final Station source, final Station target) {
        final SectionGraph sectionGraph = new SectionGraph(sections);

        return new DijkstraShortestPath<>(sectionGraph.graph())
            .getPath(source, target);
    }
}
