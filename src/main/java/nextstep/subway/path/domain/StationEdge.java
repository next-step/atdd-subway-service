package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class StationEdge extends DefaultWeightedEdge {
    
    private Section section;

    public StationEdge(Section section) {
        this.section = section;
    }

    public Section getSection() {
        return section;
    }

    @Override
    protected double getWeight() {
        return section.getDistanceValue();
    }

    @Override
    protected Object getSource() {
        return section.getUpStation();
    }

    @Override
    protected Object getTarget() {
        return section.getDownStation();
    }
}
