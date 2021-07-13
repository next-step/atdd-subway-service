package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {
    private final Section section;
    private final Line line;

    public SectionEdge(Section section, Line line) {
        this.section = section;
        this.line = line;
    }

    public Section section() {
        return section;
    }

    public Line line() {
        return line;
    }
}
