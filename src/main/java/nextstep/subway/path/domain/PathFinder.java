package nextstep.subway.path.domain;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import nextstep.subway.path.exception.NoConnectedStationsException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class PathFinder {
    private final Graph graph;

    public PathFinder(PathGraph pathGraph) {
        this.graph = pathGraph.getGraph();
    }

    public static PathFinder of(List<Line> lines) {
        PathGraph pathGraph = new PathGraph();
        lines.forEach(line -> line.addPathInfoTo(pathGraph));
        return new PathFinder(pathGraph);
    }

    public Path findPath(Station sourceStation, Station targetStation) {
        validateExistStartStationInLine(sourceStation);
        validateExistEndStationInLine(targetStation);
        DijkstraShortestPath pathMaker = new DijkstraShortestPath(this.graph);
        validateConnectedStations(pathMaker, sourceStation, targetStation);
        return new Path(pathMaker.getPath(sourceStation, targetStation));
    }

    private void validateConnectedStations(DijkstraShortestPath pathMaker, Station sourceStation, Station targetStation) {
        if (Double.isInfinite(pathMaker.getPathWeight(sourceStation, targetStation))) {
            throw new NoConnectedStationsException();
        }
    }

    private void validateExistEndStationInLine(Station targetStation) {
        if (!this.graph.containsVertex(targetStation)) {
            throw new IllegalArgumentException("도착역이 속하는 노선이 없습니다.");
        }
    }

    private void validateExistStartStationInLine(Station sourceStation) {
        if (!this.graph.containsVertex(sourceStation)) {
            throw new IllegalArgumentException("출발역이 속하는 노선이 없습니다.");
        }
    }
}
