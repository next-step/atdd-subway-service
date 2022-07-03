package nextstep.subway.path.domain;

public enum FareSalePolicy {

    BABY(0,6, 0),
    CHILDREN(5, 13, 0.5),
    TEEN(12, 19, 0.2),
    ADULT(18, Integer.MAX_VALUE, 0);

    private final int over;
    private final int under;
    private final double discountRate;

    private static final int SUBTRACT_FARE = 350;

    FareSalePolicy(int over, int under, double discountRate) {
        this.discountRate = discountRate;
        this.over = over;
        this.under = under;
    }

    private static FareSalePolicy getFarePolicyByAge(int age) {
        if (age > ADULT.over) {
            return ADULT;
        }

        if (age > TEEN.over && age < TEEN.under) {
            return TEEN;
        }

        if (age > CHILDREN.over && age < CHILDREN.under) {
            return CHILDREN;
        }

        return BABY;
    }

    public static int calculateSaleByAge(int age, int fare) {
        FareSalePolicy fareSalePolicy = getFarePolicyByAge(age);

        return (int) ((fare - SUBTRACT_FARE) * fareSalePolicy.discountRate);
    }

    public int getOver() {
        return over;
    }
}
