package nextstep.subway.path.application;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;

import static nextstep.subway.path.application.CommonMessage.MESSAGE_STATIONS_NOT_ABLE_TO_REACHED;

public class DijkstraPathFinder implements PathFindAlgorithm {
    @Override
    public List<Station> findShortestPath(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Station departStation, Station destStation) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(departStation, destStation);
        if (Objects.isNull(path)) {
            throw new IllegalArgumentException(MESSAGE_STATIONS_NOT_ABLE_TO_REACHED);
        }
        return path.getVertexList();
    }
}
