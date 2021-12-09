package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class PathFinder {

    private final Graph graph;

    public PathFinder() {
        this.graph = new DijkstraGraph();
    }

    public PathFinder(final Graph graph) {
        this.graph = graph;
    }

//    public static PathFinder of() {
//        PathFinder pathFinder = new PathFinder(new DijkstraGraph());
//        return pathFinder;
//    }

    public PathResult find(List<Line> lines, Station source, Station target) {

        if(source == target) {
            throw new IllegalStateException("출발역과 도착역이 같습니다.");
        }

        return this.graph.find(lines, source, target);
    }
}
