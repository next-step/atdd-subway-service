package nextstep.subway.path.domain;

import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class PathFinder {
    private final DijkstraShortestPath dijkstraShortestPath;

    public PathFinder(PathGraph pathGraph) {
        this.dijkstraShortestPath = new DijkstraShortestPath(pathGraph.getGraph());
    }

    public static PathFinder of(List<Line> lines) {
        PathGraph pathGraph = new PathGraph();
        lines.forEach(line -> line.setPathInfoTo(pathGraph));
        return new PathFinder(pathGraph);
    }

    public Path findPath(Station sourceStation, Station targetStation) {
        validateSameStations(sourceStation, targetStation);
        try {
            return new Path(this.dijkstraShortestPath.getPath(sourceStation, targetStation));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("구간으로 연결되지 않은 역입니다.");
        }
    }

    private void validateSameStations(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalStateException("경로조회 출발역과 도착역이 같습니다.");
        }
    }
}
