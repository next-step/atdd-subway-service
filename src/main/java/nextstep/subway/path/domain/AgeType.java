package nextstep.subway.path.domain;

public enum AgeType {

    NONE(0, 0.0),
    TEENAGER(350, 0.2),
    CHILDREN(350,0.5);

    private static final int CHILDREN_MIN_AGE = 6;
    private static final int CHILDREN_MAX_AGE = 12;
    private static final int TEENAGER_MIN_AGE = 13;
    private static final int TEENAGER_MAX_AGE = 18;

    private int discountFare;
    public double discountFarePercent;

    AgeType(int discountFare, double discountFarePercent) {
        this.discountFare = discountFare;
        this.discountFarePercent = discountFarePercent;
    }

    public static AgeType getAgeType(int age) {
        if (isChildren(age)) {
            return CHILDREN;
        }

        if (isTeenAger(age)) {
            return TEENAGER;
        }
        return NONE;
    }

    private static boolean isTeenAger(int age) {
        return age >= TEENAGER_MIN_AGE && age <= TEENAGER_MAX_AGE;
    }

    private static boolean isChildren(int age) {
        return age >= CHILDREN_MIN_AGE && age <= CHILDREN_MAX_AGE;
    }

    public int getDiscountFare() {
        return discountFare;
    }

    public double getDiscountFarePercent() {
        return discountFarePercent;
    }
}
