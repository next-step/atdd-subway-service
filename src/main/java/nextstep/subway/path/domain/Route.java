package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

public class Route {
    private final SubwayGraph subwayGraph;
    private final PathAlgorithm pathAlgorithm;

    private Route(List<Line> lines) {
        this.subwayGraph = SubwayGraph.of(lines);
        this.pathAlgorithm = PathAlgorithm.of(new DijkstraShortestPath<>(subwayGraph.getGraph()));
    }

    public Path findPath(Station sourceStation, Station targetStation) {
        validateSourceAndTargetStation(sourceStation, targetStation);
        GraphPath<Station, DefaultWeightedEdge> graphPath = pathAlgorithm.findShortestPath(sourceStation, targetStation);

        return Path.of(graphPath.getVertexList(), (int) graphPath.getWeight());
    }

    private void validateSourceAndTargetStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
    }

    public static Route of(List<Line> lines) {
        return new Route(lines);
    }

    public SubwayGraph getSubwayGraph() {
        return subwayGraph;
    }

    public PathAlgorithm getPathAlgorithm() {
        return pathAlgorithm;
    }
}
