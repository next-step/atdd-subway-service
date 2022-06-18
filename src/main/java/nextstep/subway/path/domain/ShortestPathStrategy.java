package nextstep.subway.path.domain;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.WeightedMultigraph;

public interface ShortestPathStrategy {
    Path findShortestPath(WeightedMultigraph<Station, SectionEdge> graph,
                                  Station source,
                                  Station target);
}
