package nextstep.subway.path.domain;

public enum FareAgeType {
    CHILD_AGE(6, 12, 350, 0.5),
    TEENAGER_AGE(13, 18, 350, 0.2),
    ADULT_AGE(19, Integer.MAX_VALUE, 0, 0);

    private final int minAge;
    private final int maxAge;
    private final int disCountFare;
    private final double disCountRate;

    FareAgeType(int minAge, int maxAge, int disCountFare, double disCountRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.disCountFare = disCountFare;
        this.disCountRate = disCountRate;
    }

    public static FareAgeType typeOf(int age) {
        if (age <= CHILD_AGE.maxAge && age >= CHILD_AGE.minAge) {
            return CHILD_AGE;
        }
        if (age <= TEENAGER_AGE.maxAge) {
            return TEENAGER_AGE;
        }
        return ADULT_AGE;
    }

    public int discountFare(int fare) {
        return (int) ((fare - disCountFare) * (1 - disCountRate));
    }
}
