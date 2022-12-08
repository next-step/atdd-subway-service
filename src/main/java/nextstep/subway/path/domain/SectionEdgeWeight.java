package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdgeWeight extends DefaultWeightedEdge {

    private Section section;

    public SectionEdgeWeight(Section section) {
        this.section = section;
    }

    public Section getSection() {
        return section;
    }

    @Override
    protected double getWeight() {
        return section.getDistance().toDouble();
    }
}
