package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionWeightedEdge extends DefaultWeightedEdge {
    private Section section;

    public void registerSection(Section section){
        this.section = section;
    }

    public Section getSection() {
        return section;
    }
}
