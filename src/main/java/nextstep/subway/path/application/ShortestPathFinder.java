package nextstep.subway.path.application;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Optional;

public class ShortestPathFinder {

    public static Optional<GraphPath<Station, DefaultWeightedEdge>> findShortestPath(
        Sections sections, Stations stations, Station sourceStation, Station targetStation) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        stations.forEach(graph::addVertex);
        sections.forEach(section -> graph.setEdgeWeight(
            graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance()));

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        return Optional.ofNullable(dijkstraShortestPath.getPath(sourceStation, targetStation));
    }
}
