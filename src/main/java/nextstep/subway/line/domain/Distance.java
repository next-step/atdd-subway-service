package nextstep.subway.line.domain;

import java.util.List;

public class Distance {
    private int distance;

    private Distance(List<SectionWeightedEdge> sectionEdges) {
        this.distance = calculateDistance(sectionEdges);
    }

    public static Distance of(List<SectionWeightedEdge> sectionEdges) {
        return new Distance(sectionEdges);
    }

    public int getDistance() {
        return distance;
    }

    public boolean betweenMinMax(int min, int max) {
        return min < distance && distance <= max;
    }

    private int calculateDistance(List<SectionWeightedEdge> sectionEdges) {
        return sectionEdges.stream()
                .mapToInt(SectionWeightedEdge::getDistance)
                .sum();
    }
}
