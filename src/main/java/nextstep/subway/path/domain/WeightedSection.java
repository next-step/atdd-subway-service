package nextstep.subway.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;

public class WeightedSection extends DefaultWeightedEdge {
    private final Section section;

    public WeightedSection(final Section section) {
        this.section = section;
    }

    Line getLine() {
        return section.getLine();
    }
}
