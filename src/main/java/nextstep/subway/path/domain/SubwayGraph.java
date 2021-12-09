package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.WeightedMultigraph;

public class SubwayGraph extends WeightedMultigraph<Station, SectionEdge> {
    public SubwayGraph(Class<? extends SectionEdge> edgeClass) {
        super(edgeClass);
    }
}
