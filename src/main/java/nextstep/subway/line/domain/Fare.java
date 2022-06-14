package nextstep.subway.line.domain;

import java.util.Set;

public class Fare {

    private static final int DEFAULT_FARE = 1_250;

    private int fare;

    private Fare(Age age, SectionWeightedEdges sectionEdges) {
        this.fare = DEFAULT_FARE + calculateExtraFare(sectionEdges.getLine());
        this.fare = FareDistance.calculate(this.fare, sectionEdges.getDisance());
        this.fare = FareAgeDiscount.calculate(this.fare, age);
    }

    public static Fare of(Age age, SectionWeightedEdges sectionEdges) {
        return new Fare(age, sectionEdges);
    }

    public int getFare() {
        return fare;
    }

    private int calculateExtraFare(Set<Line> lines) {
        return lines.stream()
                .mapToInt(Line::getExtraFare)
                .max().orElseThrow(RuntimeException::new);
    }
}
