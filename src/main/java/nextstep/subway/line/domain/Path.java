package nextstep.subway.line.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Optional;

public class Path {

    private final WeightedMultigraph<Station, SectionWeightedEdge> graph = new WeightedMultigraph(SectionWeightedEdge.class);

    protected Path(List<Line> lines) {
        this.createGraph(lines);
    }

    public static Path of(List<Line> lines) {
        return new Path(lines);
    }

    public PathResult findShortest(LoginMember loginMember, Station source, Station target) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return PathResult.of(loginMember, Optional.ofNullable(dijkstraShortestPath.getPath(source, target))
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
