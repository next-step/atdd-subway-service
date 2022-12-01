package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

@FunctionalInterface
public interface PathFindStrategy {

    GraphPath<Station, DefaultWeightedEdge> findShortestPath(Station source, Station target,
                                                             WeightedMultigraph<Station, DefaultWeightedEdge> graph);
}
