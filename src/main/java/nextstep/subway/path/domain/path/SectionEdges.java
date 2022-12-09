package nextstep.subway.path.domain.path;

import java.util.Collections;
import java.util.List;

public class SectionEdges {

    private final List<SectionEdge> sectionEdges;

    private SectionEdges(List<SectionEdge> sectionEdges) {
        this.sectionEdges = sectionEdges;
    }

    public static SectionEdges of(final List<SectionEdge> sectionEdges) {
        return new SectionEdges(Collections.unmodifiableList(sectionEdges));
    }

    public int getMaxSurCharge() {
        return sectionEdges.stream().mapToInt(
                it->it.getSection().getLine().getSurCharge().value()
        ).max().orElse(0);
    }


}
