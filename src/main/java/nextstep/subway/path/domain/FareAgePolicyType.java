package nextstep.subway.path.domain;

public enum FareAgePolicyType {
    BABY_AGE(0, 0, 1),
    CHILD_AGE(6, 350, 0.5),
    TEENAGER_AGE(13, 350, 0.2),
    ADULT_AGE(19, 0, 0);

    private final int minAge;
    private final int discountAmount;
    private final double discountRate;

    FareAgePolicyType(int minAge, int discountAmount, double discountRate) {
        this.minAge = minAge;
        this.discountAmount = discountAmount;
        this.discountRate = discountRate;
    }

    public static FareAgePolicyType of(int age) {
        if(ADULT_AGE.minAge <= age) {
            return ADULT_AGE;
        }
        if (TEENAGER_AGE.minAge <= age) {
            return TEENAGER_AGE;
        }
        if (CHILD_AGE.minAge <= age) {
            return CHILD_AGE;
        }
        return BABY_AGE;
    }

    public int discountFare(int fare) {
        return (int) ((fare - discountAmount) * (1 - discountRate));
    }
}
