package nextstep.subway.path.utils;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;

public class Route {
    private final WeightedMultigraph<Station, SectionEdge> graph
            = new WeightedMultigraph<>(SectionEdge.class);

    public GraphPath<Station, SectionEdge> getShortestRoute(final List<Line> lines, final Station startStation, final Station endStation) {
        if (Objects.equals(startStation, endStation)) {
            throw new IllegalStateException("출발역과 도착역을 달라야 된다.");
        }
        makeEdge(lines);
        final GraphPath<Station, SectionEdge> path = new DijkstraShortestPath<>(graph).getPath(endStation, startStation);
        if (Objects.isNull(path)) {
            throw new IllegalStateException("경로를 검색할 수 없습니다.");
        }
        return path;
    }

    private void makeEdge(final List<Line> lines) {
        lines.stream().map(line -> line.getSections().getSections()).forEach(this::setVertexAndEdgeWeight);
    }


    private void setVertexAndEdgeWeight(final List<Section> sections) {
        sections.forEach(section -> {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            SectionEdge sectionEdge = graph.addEdge(section.getUpStation(), section.getDownStation());
            graph.setEdgeWeight(sectionEdge.updateSection(section), section.getDistance().of());
        });
    }
}
