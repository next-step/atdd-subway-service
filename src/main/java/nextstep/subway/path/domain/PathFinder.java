package nextstep.subway.path.domain;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.FareCalculator;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

@Component
public class PathFinder {
    private final WeightedMultigraph<Station, SectionEdge> graph;
    private final DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath;
    private final FareCalculator fareCalculator;

    public PathFinder(FareCalculator fareCalculator) {
        graph = new WeightedMultigraph<>(SectionEdge.class);
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        this.fareCalculator = fareCalculator;
    }

    public void addLines(List<Line> lines) {
        lines.forEach(this::addLine);
    }

    public void addLine(Line line) {
        addVertex(line);
        addEdgeWeight(line);
    }

    public void removeLine(Line line) {
        line.getStations().forEach(graph::removeVertex);
    }

    private void addVertex(Line line) {
        line.getStations().forEach(graph::addVertex);
    }

    private void addEdgeWeight(Line line) {
        line.getSections()
                .stream()
                .map(section -> new SectionEdge(section))
                .forEach(edge -> {
                    graph.addEdge((Station) edge.getSource(), (Station) edge.getTarget(), edge);
                    graph.setEdgeWeight(edge, edge.getWeight());
                });
    }

    public Path findShortestPath(Station source, Station target, LoginMember loginMember) {
        GraphPath<Station, SectionEdge> graphPath = findShortestGraphPath(
                source, target);

        List<Line> lines = graphPath.getEdgeList().stream().map(SectionEdge::getLine).collect(Collectors.toList());
        Distance distance = Distance.from((int) graphPath.getWeight());

        return Path.of(graphPath.getVertexList(), distance,
                fareCalculator.calculate(distance, lines, loginMember));
    }

    private GraphPath<Station, SectionEdge> findShortestGraphPath(Station source, Station target) {
        GraphPath<Station, SectionEdge> graphPath = dijkstraShortestPath.getPath(source, target);

        if (graphPath == null) {
            throw new IllegalArgumentException("경로가 없습니다.");
        }
        return graphPath;
    }
}
