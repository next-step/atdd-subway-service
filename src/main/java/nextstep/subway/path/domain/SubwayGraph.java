package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.WeightedMultigraph;

public class SubwayGraph extends WeightedMultigraph<Station, SectionEdge> {
    public SubwayGraph(Class<? extends SectionEdge> edgeClass) {
        super(edgeClass);
    }

    public void addEdge(Section section, Station source, Station target, Fare extraFare) {
        SectionEdge newSectionEdge = new SectionEdge(section, source, target, section.getDistanceValue(), extraFare);
        super.addEdge(newSectionEdge.getSource(), newSectionEdge.getTarget(), newSectionEdge);
        super.setEdgeWeight(newSectionEdge, newSectionEdge.getWeight());
    }

}
