package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.ui.SameSourceTargetException;
import nextstep.subway.path.ui.SourceTargetNotConnectException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class PathFinder {

    private DijkstraShortestPath dijkstraShortestPath;

    public PathFinder(List<Station> stations, Lines lines) {

        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph(SectionEdge.class);

        addStationToVertex(stations, graph);
        addSectionToEdge(lines, graph);

        dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    private void addStationToVertex(List<Station> allStations, WeightedMultigraph<Station, SectionEdge> graph) {
        for (Station station : allStations) {
            graph.addVertex(station);
        }
    }

    private void addSectionToEdge(Lines lines, WeightedMultigraph<Station, SectionEdge> graph) {
        lines.handleLinesSection(section -> graph.setEdgeWeight(
                createGraph(graph, section),
                section.getDistance()
        ));
    }

    private SectionEdge createGraph(WeightedMultigraph<Station, SectionEdge> graph, Section section) {
        SectionEdge sectionEdge = graph.addEdge(section.upStation(), section.downStation());
        sectionEdge.addSection(section);
        return sectionEdge;
    }

    public List<Station> shortestPath(Station source, Station target) {
        try {
            return graphPath(source, target).getVertexList();
        } catch (NullPointerException e) {
            throw new SourceTargetNotConnectException();
        }
    }

    public int shortestWeight(Station source, Station target) {
        double distance;

        try {
            distance = graphPath(source, target).getWeight();
        } catch (NullPointerException e) {
            throw new SourceTargetNotConnectException();
        }

        return (int) distance;
    }

    private GraphPath<Station, SectionEdge> graphPath(Station source, Station target) {
        validateSourceTarget(source, target);

        return dijkstraShortestPath.getPath(source, target);
    }

    private void validateSourceTarget(Station source, Station target) {
        if (source.isSameStation(target)) {
            throw new SameSourceTargetException();
        }
    }

    public List<Line> goThroughLines(Station source, Station target) {
        return graphPath(source, target)
                .getEdgeList()
                .stream()
                .map(sectionEdge -> sectionEdge.getSection().getLine())
                .distinct()
                .collect(Collectors.toList());
    }
}
