package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Optional;

public class Path {

    final private WeightedMultigraph<Station, SectionWeightedEdge> graph = new WeightedMultigraph(SectionWeightedEdge.class);

    protected Path() {
    }

    static public Path of() {
        return new Path();
    }

    public PathResult findShortest(List<Line> lines, Station source, Station target) {
        this.createGraph(lines);
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return new PathResult(Optional.ofNullable(dijkstraShortestPath.getPath(source, target))
                .orElseThrow(() -> new RuntimeException("출발역과 도착역이 연결되어 있지 않음")));
    }

    private void createGraph(List<Line> lines) {
        for (Line line : lines) {
            addSection(line);
        }
    }

    private void addSection(Line line) {
        for (Section section : line.getSections()) {
            SectionWeightedEdge edge = SectionWeightedEdge.of(section);
            graph.addVertex(edge.getUpStation());
            graph.addVertex(edge.getDownStation());
            graph.addEdge(edge.getUpStation(), edge.getDownStation(), edge);
            graph.setEdgeWeight(edge, edge.getDistance());
        }
    }
}
