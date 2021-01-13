package nextstep.subway.path.application.fare;

import java.util.Arrays;

public enum AgeDrawbackFarePolicy {
    CHILD(350, 0.5, 5, 13),
    YOUTH(350, 0.2, 13, 19),
    ADULT(0, 0, 19, Integer.MAX_VALUE);

    private final int drawbackAmout;
    private final double drawbackPercent;
    private final int minAge;
    private final int maxAge;

    AgeDrawbackFarePolicy(int drawbackAmout, double drawbackPercent, int minAge, int maxAge) {
        this.drawbackAmout = drawbackAmout;
        this.drawbackPercent = drawbackPercent;
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public static AgeDrawbackFarePolicy valueOf(int age) {
        return Arrays.stream(AgeDrawbackFarePolicy.values())
                .filter(filteredRank -> filteredRank.valid(age))
                .findFirst()
                .orElse(ADULT);
    }

    private boolean valid(int age) {
        return age >= minAge && age < maxAge;
    }

    public int calculateDrawbackFare(int fare) {
        return (int) ((fare - drawbackAmout) * drawbackPercent);
    }
}
