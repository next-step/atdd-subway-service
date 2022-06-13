package nextstep.subway.line.domain;

import java.util.List;

public class Fare {

    private static final int DEFAULT_FARE = 1_250;

    private int fare;

    private Fare(Distance distance, Age age, List<SectionWeightedEdge> sectionEdges) {
        this.fare = DEFAULT_FARE + calculateExtraFare(sectionEdges);
        this.fare = FareDistance.calculate(this.fare, distance);
        this.fare = FareAgeDiscount.calculate(this.fare, age);
    }

    public static Fare of(Distance distance, Age age, List<SectionWeightedEdge> sectionEdges) {
        return new Fare(distance, age, sectionEdges);
    }

    public int getFare() {
        return fare;
    }

    private int calculateExtraFare(List<SectionWeightedEdge> sectionEdges) {
        return sectionEdges.stream()
                .mapToInt(SectionWeightedEdge::getExtraFare)
                .max().orElseThrow(RuntimeException::new);
    }
}
