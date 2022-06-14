package nextstep.subway.line.domain;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SectionWeightedEdges {
    private final List<SectionWeightedEdge> sectionEdges;

    private SectionWeightedEdges(List<SectionWeightedEdge> sectionEdges) {
        this.sectionEdges = sectionEdges;
    }

    public static SectionWeightedEdges of(List<SectionWeightedEdge> edges) {
        return new SectionWeightedEdges(edges);
    }

    public Set<Line> getLine() {
        return sectionEdges.stream()
                .map(SectionWeightedEdge::getLine)
                .collect(Collectors.toSet());
    }

    public Distance getDisance() {
        return Distance.of(sectionEdges);
    }
}
