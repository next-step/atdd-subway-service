package nextstep.subway.line.domain;

import java.util.Set;

public class Fare {

    private static final int DEFAULT_FARE = 1_250;
    private static final int DEDUCTION_FEE = 350;

    private int fare;

    private Fare(Age age, SectionWeightedEdges sectionEdges) {
        this.fare = DEFAULT_FARE
                + getExtraFare(sectionEdges.getLine())
                + getDistanceFare(sectionEdges.getDisance());
        calculateAgeDiscount(fare, age);
    }

    public static Fare of(Age age, SectionWeightedEdges sectionEdges) {
        return new Fare(age, sectionEdges);
    }

    public int getFare() {
        return fare;
    }

    private int getExtraFare(Set<Line> lines) {
        return lines.stream()
                .mapToInt(Line::getExtraFare)
                .max().orElseThrow(RuntimeException::new);
    }

    private int getDistanceFare(Distance distance) {
        FareDistance fareDistance = FareDistance.of(distance);
        int overDistance = distance.getDistance() - fareDistance.getMinDistance();
        return (int) Math.ceil(overDistance / fareDistance.getOverFareDistance()) *
                fareDistance.getOverFarePrice();
    }

    private void calculateAgeDiscount(int fare, Age age) {
        if (age.isNotAdult()) {
            this.fare -= getAgeDiscount(fare, age);
        }
    }

    private int getAgeDiscount(int fare, Age age) {
        FareAgeDiscount ageDiscount = FareAgeDiscount.of(age.getValue());
        return (int) ((fare - DEDUCTION_FEE) * ageDiscount.getDiscountRate());
    }
}
