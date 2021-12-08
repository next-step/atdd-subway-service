package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public interface ShortestPathAlgorithm {
    void init(WeightedMultigraph<Station, DefaultWeightedEdge> graph);

    GraphPath<Station, DefaultWeightedEdge> getPath(Station source, Station target);
}
