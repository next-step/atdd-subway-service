package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.WeightedMultigraph;

public class StationGraph extends WeightedMultigraph<Station, SectionEdge> {
    public StationGraph(Class<? extends SectionEdge> edgeClass) {
        super(edgeClass);
    }
}
