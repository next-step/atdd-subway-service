package nextstep.subway.path.dto;

import org.jgrapht.graph.DefaultWeightedEdge;

import nextstep.subway.line.domain.Section;

public class SubwayWeightedEdge extends DefaultWeightedEdge {
    Section section;
        
    public SubwayWeightedEdge(Section section) {
        super();

        this.section = section;
    }

    public Section getSection() {
        return this.section;
    }
}
