package nextstep.subway.path.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import nextstep.subway.common.exception.SubwayErrorCode;
import nextstep.subway.common.exception.SubwayException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Stations;
import nextstep.subway.station.domain.Station;

public class PathFinder {
    private final Stations stations;
    private final Distance distance;

    public PathFinder(StationGraph graph, Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> path = makePath(graph, source, target);
        this.stations = new Stations(path.getVertexList());
        this.distance = new Distance((int)path.getWeight());
    }

    private GraphPath<Station, DefaultWeightedEdge> makePath(StationGraph graph,
        Station source,
        Station target) {
        validateSourceAndTargetDifferent(source, target);

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = graph.getDijkstraShortestPath();

        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
        validateSourceAndTargetConnected(path);

        return path;
    }

    private void validateSourceAndTargetDifferent(Station source, Station target) {
        if (source.equals(target)) {
            throw new SubwayException(SubwayErrorCode.SAME_SOURCE_AND_TARGET);
        }
    }

    private void validateSourceAndTargetConnected(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new SubwayException(SubwayErrorCode.NOT_CONNECTED_SOURCE_AND_TARGET);
        }
    }

    public Stations getStations() {
        return stations;
    }

    public Distance getDistance() {
        return distance;
    }

}
