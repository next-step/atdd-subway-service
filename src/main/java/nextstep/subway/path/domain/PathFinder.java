package nextstep.subway.path.domain;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import nextstep.subway.common.exception.SubwayErrorCode;
import nextstep.subway.common.exception.SubwayException;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.station.dto.StationResponses;

public class PathFinder {
    private final GraphPath<Station, DefaultWeightedEdge> path;

    public PathFinder(StationGraph graph, Station source, Station target) {
        this.path = makePath(graph, source, target);
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

    public PathResponse findPath() {
        List<Station> stations = path.getVertexList();

        List<StationResponse> responses = StationResponses.from(stations)
            .getResponses();

        int distance = (int)path.getWeight();

        return new PathResponse(responses, distance);
    }
}
