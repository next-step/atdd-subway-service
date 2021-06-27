package nextstep.subway.path.domain;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import nextstep.subway.path.exception.NoConnectedStationsException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class PathFinder {
    private final Graph graph;
    private final DijkstraShortestPath pathMaker;

    private PathFinder(Graph graph, DijkstraShortestPath pathMaker) {
        this.graph = graph;
        this.pathMaker = pathMaker;
    }

    public static PathFinder of(List<Line> lines) {
        PathGraph pathGraph = new PathGraph();
        lines.forEach(line -> line.addPathInfoTo(pathGraph));
        return new PathFinder(pathGraph.getGraph(), new DijkstraShortestPath(pathGraph.getGraph()));
    }

    public Path findPath(Station sourceStation, Station targetStation) {
        validateExistStartStationInLine(sourceStation);
        validateExistEndStationInLine(targetStation);
        validateConnectedStations(sourceStation, targetStation);
        return new Path(this.pathMaker.getPath(sourceStation, targetStation));
    }

    public boolean isConnectedPath(Station sourceStation, Station targetStation) {
        return this.graph.containsVertex(sourceStation) && this.graph.containsVertex(targetStation)
                && Double.isFinite(this.pathMaker.getPathWeight(sourceStation, targetStation));
    }

    private void validateConnectedStations(Station sourceStation, Station targetStation) {
        if (Double.isInfinite(this.pathMaker.getPathWeight(sourceStation, targetStation))) {
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
