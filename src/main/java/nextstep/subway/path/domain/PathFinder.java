package nextstep.subway.path.domain;

import java.util.List;
import java.util.Optional;
import nextstep.subway.exception.NotLinkedPathException;
import nextstep.subway.exception.SamePathException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {
    private final WeightedMultigraph<Station, SectionWeightedEdge> graph;

    private PathFinder() {
        this.graph = new WeightedMultigraph<>(SectionWeightedEdge.class);
    }

    public static PathFinder create() {
        return new PathFinder();
    }

    public void init(List<Line> lines) {
        lines.forEach(this::addVertexAndEdge);
    }

    private void addVertexAndEdge(Line line) {
        addVertex(line.getSections());
        addEdgeWeight(line.getSections());
    }

    private void addVertex(Sections sections) {
        sections.getStations()
                .forEach(graph::addVertex);
    }

    private void addEdgeWeight(Sections sections) {
        sections.getSections()
                .forEach(this::addEdgeAndSetWeight);
    }

    private double weight(Distance distance) {
        return distance.getDistance();
    }

    private void addEdgeAndSetWeight(Section section) {
        final SectionWeightedEdge sectionWeightedEdge = new SectionWeightedEdge(section);
        graph.addEdge(section.upStation(), section.downStation(), sectionWeightedEdge);
        graph.setEdgeWeight(sectionWeightedEdge, weight(section.getDistance()));
    }

    public GraphPath<Station, SectionWeightedEdge> getDijkstraPath(Station source, Station target) {
        validateSameSourceAndTarget(source, target);
        return getOptionalDijkstraPath(source, target)
                .orElseThrow(NotLinkedPathException::new);
    }

    public void validatePath(Station source, Station target) {
        getDijkstraPath(source, target);
    }

    private Optional<GraphPath<Station, SectionWeightedEdge>> getOptionalDijkstraPath(Station source, Station target) {
        DijkstraShortestPath<Station, SectionWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return Optional.ofNullable(dijkstraShortestPath.getPath(source, target));
    }

    private void validateSameSourceAndTarget(Station source, Station target) {
        if (source.equals(target)) {
            throw new SamePathException();
        }
    }
}
