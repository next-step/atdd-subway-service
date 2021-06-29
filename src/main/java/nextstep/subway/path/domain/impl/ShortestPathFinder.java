package nextstep.subway.path.domain.impl;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.SectionEdge;
import nextstep.subway.path.exception.SameOriginAndDestinationException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;


@Component
public class ShortestPathFinder implements PathFinder {

    private WeightedMultigraph<Station, SectionEdge> graph;

    public ShortestPath findPath(Lines lines, Station source, Station target) {
        graph = new WeightedMultigraph<>(SectionEdge.class);
        addVertexs(lines.getStations());
        addEdges(lines.getSections());

        if (source.equals(target)) {
            throw new SameOriginAndDestinationException();
        }
        return new ShortestPath(new DijkstraShortestPath<>(graph).getPath(source, target));
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
}
