package nextstep.subway.path.application;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.Distance;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class ShortestPathFinder {
    private static final WeightedMultigraph<Station, DefaultWeightedEdge> GRAPH;
    private static final DijkstraShortestPath<Station, DefaultWeightedEdge> DIJKSTRA_SHORTEST_PATH;

    static {
        GRAPH = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        DIJKSTRA_SHORTEST_PATH = new DijkstraShortestPath<>(GRAPH);
    }

    public static Path findShortestPath(
        Sections sections, Stations stations, Station sourceStation, Station targetStation) {

        stations.forEach(GRAPH::addVertex);
        sections.forEach(section -> {
            DefaultWeightedEdge edge = GRAPH.addEdge(section.getUpStation(), section.getDownStation());
            GRAPH.setEdgeWeight(edge, section.getDistance());
        });

        GraphPath<Station, DefaultWeightedEdge> shortestPath = DIJKSTRA_SHORTEST_PATH.getPath(sourceStation, targetStation);
        checkPathIsNull(shortestPath);

        return new Path(shortestPath.getVertexList(), new Distance((int) shortestPath.getWeight()));
    }

    private static void checkPathIsNull(GraphPath<Station, DefaultWeightedEdge> shortestPath) {
        if (shortestPath == null) {
            throw new PathFindException("source station is not connected to target station");
        }
    }
}
