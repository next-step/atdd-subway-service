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

    public static Path findShortestPath(
        Sections sections, Stations stations, Station sourceStation, Station targetStation) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        stations.forEach(graph::addVertex);
        sections.forEach(section -> graph.setEdgeWeight(
            graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance()));

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
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
