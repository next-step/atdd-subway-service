package nextstep.subway.utils;

import nextstep.subway.station.domain.Station;
import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.WeightedMultigraph;

public class SubwayGraph<V, E> extends WeightedMultigraph<Station, SectionEdge> {

    public SubwayGraph(EdgeFactory<Station, SectionEdge> ef) {
        super(ef);
    }

    public SubwayGraph(Class<? extends SectionEdge> edgeClass) {
        super(edgeClass);
    }
}
