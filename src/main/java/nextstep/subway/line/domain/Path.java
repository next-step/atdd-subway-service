package nextstep.subway.line.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

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
        GraphPath graphPath = dijkstraShortestPath.getPath(source, target);

        if (null == graphPath) {
            throw new RuntimeException("출발역과 도착역이 연결되어 있지 않습니다.");
        }

        SectionWeightedEdges edges = SectionWeightedEdges.of(graphPath.getEdgeList());
        return PathResult.of(graphPath.getVertexList(), edges.getDisance(), Fare.of(loginMember.getAge(), edges));
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
