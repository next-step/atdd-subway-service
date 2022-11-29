package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.exception.InvalidPathException;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static nextstep.subway.line.exception.InvalidPathException.SOURCE_AND_TARGET_EQUAL;
import static nextstep.subway.line.exception.InvalidPathException.STATION_NOT_EXISTS;

public class PathFinder {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public PathFinder(List<Line> lines) {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        lines.forEach(line -> line.addPath(this));
    }

    public PathResponse find(Station sourceStation, Station targetStation) {
        verifyPath(sourceStation, targetStation);

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        GraphPath<Station, DefaultWeightedEdge> shortestDistancePath = findPath(sourceStation, targetStation, dijkstraShortestPath);

        List<Station> shortestPathStations = shortestDistancePath.getVertexList();
        Distance distance = new Distance(dijkstraShortestPath.getPathWeight(sourceStation, targetStation));

        return new PathResponse(shortestPathStations, distance);
    }

    private GraphPath<Station, DefaultWeightedEdge> findPath(Station sourceStation, Station targetStation,
                                                             DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath) {
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(sourceStation, targetStation);
        verifyPathExists(path);
        return path;

    }

    private static void verifyPathExists(GraphPath<Station, DefaultWeightedEdge> path) {
        if (Objects.isNull(path)) {
            throw new InvalidPathException(InvalidPathException.SOURCE_AND_TARGET_NOT_CONNECTED);
        }
    }

    private void verifyPath(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new InvalidPathException(SOURCE_AND_TARGET_EQUAL);
        }

        Set<Station> stations = graph.vertexSet();
        if (!stations.contains(sourceStation) || !stations.contains(targetStation)) {
            throw new InvalidPathException(STATION_NOT_EXISTS);
        }
    }

    public void addPath(Station upStation, Station downStation, Distance distance) {
        graph.addVertex(upStation);
        graph.addVertex(downStation);
        graph.setEdgeWeight(graph.addEdge(upStation, downStation), distance.toDouble());
    }
}
