package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;


public class PathFinder {
    private DijkstraShortestPath dijkstraShortestPath;
    private static long additionalFare = 0L;

    private PathFinder(DijkstraShortestPath dijkstraShortestPath) {
        this.dijkstraShortestPath = dijkstraShortestPath;
    }

    public static PathFinder initialPathFinder(List<Line> lines) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);

        lines.stream().forEach(line -> {
            line.getSectionList().forEach(section -> {
                graph.addVertex(section.getUpStation().getId());
                graph.addVertex(section.getDownStation().getId());
                graph.setEdgeWeight(
                        graph.addEdge(section.getUpStation().getId(), section.getDownStation().getId()), section.getDistance()
                );
            });
            additionalFare = line.getAdditionalFare() > additionalFare ? line.getAdditionalFare() : additionalFare;
        });
        return new PathFinder(new DijkstraShortestPath(graph));
    }

    public GraphPath getShortestPath(Long sourceId, Long targetId) {
        return dijkstraShortestPath.getPath(sourceId, targetId);
    }

    public Fare getFare(double distance) {
        Fare fare = new Fare();
        fare.addFee(additionalFare);
        if(distance - 10 > 0) {
            fare.addFee(calculateOverFare((int) distance - 10));
        }
        return fare;
    }

    private long calculateOverFare(int distance) {
        if(distance > 40) {
            return (long) ((Math.ceil((distance - 1) / 8) + 1) * 100);
        }
        return (long) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }
}
