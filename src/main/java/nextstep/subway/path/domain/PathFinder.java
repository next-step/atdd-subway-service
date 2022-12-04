package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.exception.InvalidPathException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static nextstep.subway.line.exception.InvalidPathException.SOURCE_AND_TARGET_EQUAL;
import static nextstep.subway.line.exception.InvalidPathException.STATION_NOT_EXISTS;

public class PathFinder {
    private final WeightedGraph<Station, SectionEdgeWeight> graph;

    public PathFinder(List<Line> lines) {
        graph = new SimpleDirectedWeightedGraph<>(SectionEdgeWeight.class);
        lines.forEach(line -> line.addPath(this));
    }

    public Path find(Station sourceStation, Station targetStation) {
        verifyPath(sourceStation, targetStation);

        DijkstraShortestPath<Station, SectionEdgeWeight> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        GraphPath<Station, SectionEdgeWeight> shortestDistancePath = findPath(sourceStation, targetStation, dijkstraShortestPath);

        List<SectionEdgeWeight> edges = shortestDistancePath.getEdgeList();
        List<Station> shortestPathStations = shortestDistancePath.getVertexList();
        Distance distance = new Distance(dijkstraShortestPath.getPathWeight(sourceStation, targetStation));

        return new Path(edges, shortestPathStations, distance);
    }

    public void addPath(Section section) {
        graph.addVertex(section.getUpStation());
        graph.addVertex(section.getDownStation());
        graph.addEdge(section.getUpStation(), section.getDownStation(), new SectionEdgeWeight(section));
    }

    private GraphPath<Station, SectionEdgeWeight> findPath(Station sourceStation, Station targetStation,
                                                             DijkstraShortestPath<Station, SectionEdgeWeight> dijkstraShortestPath) {
        GraphPath<Station, SectionEdgeWeight> path = dijkstraShortestPath.getPath(sourceStation, targetStation);
        verifyPathExists(path);
        return path;
    }

    private static void verifyPathExists(GraphPath<Station, SectionEdgeWeight> path) {
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
}
