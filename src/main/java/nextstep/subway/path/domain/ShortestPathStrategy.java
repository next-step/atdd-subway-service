package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public interface ShortestPathStrategy {
    Path findShortestPath(WeightedMultigraph<Station, DefaultWeightedEdge> graph,
                              Station source,
                              Station target);
}
