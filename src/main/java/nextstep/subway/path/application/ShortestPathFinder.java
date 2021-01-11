package nextstep.subway.path.application;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class ShortestPathFinder {
    private static final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private static final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    static {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    public static Path findShortestPath(
        Sections sections, Stations stations, Station sourceStation, Station targetStation) {

        stations.forEach(graph::addVertex);
        sections.forEach(section -> {
            DefaultWeightedEdge edge = graph.addEdge(section.getUpStation(), section.getDownStation());
            graph.setEdgeWeight(edge, section.getDistance());
        });

        GraphPath<Station, DefaultWeightedEdge> shortestPath = dijkstraShortestPath.getPath(sourceStation, targetStation);
        checkPathIsNull(shortestPath);

        return new Path(shortestPath.getVertexList(), (int) shortestPath.getWeight());
    }

    private static void checkPathIsNull(GraphPath<Station, DefaultWeightedEdge> shortestPath) {
        if (shortestPath == null) {
            throw new PathFindException("source station is not connected to target station");
        }
    }
}
