package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {
    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public PathFinder(Lines lines, Station source, Station target) {
        graph = new WeightedMultigraph(DefaultWeightedEdge.class);
    }
}
