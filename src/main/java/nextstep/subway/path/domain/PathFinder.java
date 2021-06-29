package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.exception.SameOriginAndDestinationException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private final WeightedMultigraph<Station, SectionEdge> graph;
    private final DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath;

    public PathFinder(Lines lines) {
        graph = new WeightedMultigraph<>(SectionEdge.class);
        addVertexs(lines.getStations());
        addEdges(lines.getSections());
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    private void addVertexs(List<Station> stations) {
        stations.forEach(graph::addVertex);
    }

    private void addEdges(List<Section> sections) {
        sections.forEach(this::setEdgeWeight);
    }

    private void setEdgeWeight(Section section) {
        Line line = section.getLine();
        SectionEdge sectionEdge = new SectionEdge(line.getSurchargeAmount());
        graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
        graph.setEdgeWeight(sectionEdge, section.getDistanceWeight());
    }

    public ShortestPath findPath(Station source, Station target) {
        if (source.equals(target)) {
            throw new SameOriginAndDestinationException();
        }
        return new ShortestPath(dijkstraShortestPath.getPath(source, target));
    }
}
