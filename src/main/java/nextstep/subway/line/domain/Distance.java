package nextstep.subway.line.domain;

import java.util.List;

public class Distance {
    private int distance;

    private Distance(List<SectionWeightedEdge> sectionEdges) {
        this.distance = culculateDistance(sectionEdges);
    }

    public static Distance of(List<SectionWeightedEdge> sectionEdges) {
        return new Distance(sectionEdges);
    }

    public int getDistance() {
        return distance;
    }

    private int culculateDistance(List<SectionWeightedEdge> sectionEdges) {
        return sectionEdges.stream()
                .mapToInt(SectionWeightedEdge::getDistance)
                .sum();
    }
}
