package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.WeightedMultigraph;

public class SubwayGraph extends WeightedMultigraph<Station, SectionEdge> {

    public SubwayGraph(Class<? extends SectionEdge> edgeClass) {
        super(edgeClass);
    }

    public void addEdge(final Section section) {
        SectionEdge sectionEdge = new SectionEdge(section);
        super.addEdge(sectionEdge.getSource(), sectionEdge.getTarget(), sectionEdge);
        super.setEdgeWeight(sectionEdge, sectionEdge.getWeight());
    }

}
