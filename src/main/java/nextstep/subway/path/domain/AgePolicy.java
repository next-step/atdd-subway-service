package nextstep.subway.path.domain;

import java.util.Arrays;

public enum AgePolicy {
    CHILD(6, 13, new Fare(350L), 0.5),
    TEENAGER(13, 19, new Fare(350L), 0.8),
    NONE(-1, -1, new Fare(0L), 1);

    private int min;
    private int max;
    private Fare minus;
    private double rate;

    AgePolicy(int min, int max, Fare minus, double rate) {
        this.min = min;
        this.max = max;
        this.minus = minus;
        this.rate = rate;
    }

    public static AgePolicy valueOf(int age) {
        return Arrays.stream(AgePolicy.values())
            .filter(agePolicy -> agePolicy.isApply(age))
            .findFirst()
            .orElse(NONE);
    }

    private boolean isApply(int age) {
        return min <= age && age < max;
    }

    public Fare discount(Fare fare) {
        return fare.minus(minus).rate(rate);
    }
}
