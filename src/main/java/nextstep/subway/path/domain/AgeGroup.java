package nextstep.subway.path.domain;

import java.util.Arrays;

public enum AgeGroup {
    ADULT(20, 65, 1, 0) {
        @Override
        public int discountFare(int fare) {
            return fare;
        }
    },
    TEENAGER(14, 19, 0.8, 0) {
        @Override
        public int discountFare(int fare) {
            return (int) ((fare - 350) * rate());
        }
    },
    CHILD(0, 13, 0.5, 0) {
        @Override
        public int discountFare(int fare) {
            return (int) ((fare - 350) * rate());
        }
    };

    AgeGroup(int from, int to, double rate, int  deduction) {
        this.from = from;
        this.to = to;
        this.rate = rate;
        this.deduction =  deduction;
    }

    private final int from;
    private final int to;
    private final double rate;
    private final int deduction;

    public int from() {
        return from;
    }

    public int to() {
        return to;
    }

    public double rate() {
        return rate;
    }

    public int deduction() {
        return deduction;
    }

    abstract public int discountFare(int fare);

    public static AgeGroup findAgeGroup(int age) {
        return Arrays.stream(values())
                     .filter(ageGroup -> ageGroup.from <= age && age <= ageGroup.to)
                     .findFirst()
                     .orElseThrow(IllegalArgumentException::new);
    }
}
