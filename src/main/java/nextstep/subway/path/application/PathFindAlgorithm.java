package nextstep.subway.path.application;

import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public interface PathFindAlgorithm {
    List<Station> findShortestPath(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Station departStation, Station destStation);
}
