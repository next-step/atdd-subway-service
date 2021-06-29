package nextstep.subway.line.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class SectionMultigraph<V, E> extends WeightedMultigraph<V, E> {

    public SectionMultigraph(Class<? extends E> edgeClass) {
        super(edgeClass);
    }

    public void setEdgeAdditionalFare(E e, double weight, int additionalFare) {
        assert (e instanceof DefaultWeightedEdge) : e.getClass();

        ((SectionEdge) e).weight = weight;
        ((SectionEdge) e).additionalFare = additionalFare;
    }
}
