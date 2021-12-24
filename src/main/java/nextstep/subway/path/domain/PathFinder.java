package nextstep.subway.path.domain;

import com.google.common.collect.Lists;
import nextstep.subway.line.domain.*;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PathFinder {
    private final Lines lines;

    public PathFinder(Line line) {
        this(Lists.newArrayList(line));
    }

    public PathFinder(List<Line> lines) {
        this.lines = new Lines(lines);
    }

    public Path findPath(Station source, Station target) {
        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph(SectionEdge.class);
        List<Station> stations = lines.getStations();
        for (Station station : stations) {
            graph.addVertex(station);
        }

        List<Section> sections = lines.getSections();
        for (Section section : sections) {
            SectionEdge sectionEdge = graph.addEdge(section.getUpStation(), section.getDownStation());
            sectionEdge.setSection(section);
            graph.setEdgeWeight(sectionEdge, section.getDistance());
        }
        return findShortestPath(source, target, graph);
    }

    private Path findShortestPath(Station source, Station target, WeightedMultigraph<Station, SectionEdge> graph) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath<Station, SectionEdge> graphPath;
        try {
            graphPath = dijkstraShortestPath.getPath(source, target);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("출발역 또는 도착역이 노선에 존재하지 않습니다.");
        }

        if (Objects.isNull(graphPath)) {
            throw new IllegalArgumentException("경로를 찾을 수 없습니다.");
        }

        Double weight = graphPath.getWeight();
        Sections sections = new Sections(graphPath.getEdgeList()
                .stream()
                .map(sectionEdge -> sectionEdge.getSection())
                .collect(Collectors.toList()));
        return new Path(graphPath.getVertexList(), new Distance(weight.intValue()), sections);
    }
}