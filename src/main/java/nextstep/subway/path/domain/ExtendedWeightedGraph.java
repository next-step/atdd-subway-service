package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.WeightedMultigraph;

/* 참고 : https://www.baeldung.com/jgrapht */
public class ExtendedWeightedGraph<V, E> extends WeightedMultigraph {

    public ExtendedWeightedGraph(Class<? extends E> edgeClass) {
        super(edgeClass);
    }

    public SectionEdge addEdgeWithExtraCharge(Section section) {
        SectionEdge sectionEdge = (SectionEdge) super.addEdge(section.getUpStation(), section.getDownStation());
        sectionEdge.extraCharge = section.getExtraCharge();
        return sectionEdge;
    }
}
