package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionWeightedEdge extends DefaultWeightedEdge {
    private final Section section;
    private final String source;
    private final String target;

    public SectionWeightedEdge( String source, String target, Section section) {
        this.source = source;
        this.target = target;
        this.section = section;
    }

    public Section getSection() {
        return section;
    }

    public int getExtraCharge() {
        return section.getLine()
                .getExtraCharge();
    }

    @Override
    protected String getSource() {
        return source;
    }

    @Override
    protected String getTarget() {
        return target;
    }

    @Override
    protected double getWeight() {
        return section.getDistance();
    }
}
