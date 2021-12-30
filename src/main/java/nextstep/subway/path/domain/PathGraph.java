package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PathGraph {
    private final WeightedMultigraph<Station, SectionEdge> graph;

    public PathGraph() {
        this.graph = new WeightedMultigraph(SectionEdge.class);
    }

    public void makeGraph(Lines lines) {
        addVertexOfStation(lines.getStations());
        addEdgeOfSections(lines.getSections());
    }

    private void addVertexOfStation(List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void addEdgeOfSections(List<Section> sections) {
        for (Section section : sections) {
            SectionEdge sectionEdge = graph.addEdge(section.getUpStation(), section.getDownStation());
            sectionEdge.setSection(section);
            graph.setEdgeWeight(sectionEdge, section.getDistance());
        }
    }

    public Path findShortestPath(Station source, Station target) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(this.graph);
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
