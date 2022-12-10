package nextstep.subway.auth.domain;

public enum AgeGroup {

    NO_SALE_AGE(0, 1), YOUTH(350, 0.8), CHILD(350, 0.5);

    private final int deductFare;
    private final double sale;

    AgeGroup(int deductFare, double sale) {
        this.deductFare = deductFare;
        this.sale = sale;
    }

    public static AgeGroup calculate(Integer age) {
        if (age >= 6 && age < 13) {
            return CHILD;
        }
        if (age >= 13 && age < 19) {
            return YOUTH;
        }
        return NO_SALE_AGE;
    }

    public double getSale() {
        return sale;
    }

    public int getDeductFare() {
        return deductFare;
    }
}
