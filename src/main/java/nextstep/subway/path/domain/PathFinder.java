package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.Objects;

public class PathFinder {
    private static DijkstraShortestPath path;

    public PathFinder(DijkstraShortestPath path) {
        this.path = path;
    }

    public Path findPath(Station startStation, Station endStation) {
        GraphPath path = PathFinder.path.getPath(startStation, endStation);
        validatePath(path);
        return new Path(path.getVertexList(), (int) PathFinder.path.getPathWeight(startStation, endStation));
    }

    private void validatePath(GraphPath path) {
        if (isNull(path)) {
            throw new IllegalArgumentException("경로가 존재하지 않습니다.");
        }
    }

    private boolean isNull(GraphPath path) {
        return Objects.isNull(path);
    }


}
