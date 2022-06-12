package nextstep.subway.line.domain;

import java.util.List;

public class Fare {

    private static final int DEFAULT_FARE = 1_250;
    private static final int DEDUCTION_FEE = 350;

    private int fare;

    private Fare(int distance, int age, List<SectionWeightedEdge> sectionEdges) {
        this.fare = DEFAULT_FARE + calculateOverFare(distance) + getExtraFare(sectionEdges);
        calulateAgeFare(age);
    }

    public static Fare of(int distance, int age, List<SectionWeightedEdge> sectionEdges) {
        return new Fare(distance, age, sectionEdges);
    }

    public int getFare() {
        return fare;
    }

    private int getExtraFare(List<SectionWeightedEdge> sectionEdges) {
        return sectionEdges.stream()
                .mapToInt(SectionWeightedEdge::getExtraFare)
                .max().orElse(0);
    }

    private int calculateOverFare(int distance) {
        if (distance > 10 && distance < 50) {
            return (int) (Math.ceil((distance - 10) / 5) * 100);
        } else if (distance > 50) {
            return (int) (Math.ceil((distance - 50) / 8) * 100);
        }
        return 0;
    }

    private void calulateAgeFare(int age) {
        if (age >= 6 && age < 13) {
            this.fare -= (this.fare - DEDUCTION_FEE) * 0.05;
        } else if (age >= 13 && age < 19) {
            this.fare -= (this.fare - DEDUCTION_FEE) * 0.02;
        }
    }
}
