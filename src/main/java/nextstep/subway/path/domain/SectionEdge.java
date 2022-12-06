package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    private Section section;

    public SectionEdge(Section section) {
        this.section = section;
    }
    public int getLineSurCharge(){
        return section.getLine().getSurCharge().value();
    }
}
