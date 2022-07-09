package nextstep.subway.charge.domain;

public enum AgeType {
    NORMAL(1.0, 0),
    YOUTH(0.8, 350),
    KID(0.5, 350);

    private final double discountRatio;
    private final int discountCharge;

    AgeType(double chargeRatio, int discountCharge) {
        this.discountRatio = chargeRatio;
        this.discountCharge = discountCharge;
    }

    public double getDiscountRatio() {
        return discountRatio;
    }

    public int getDiscountCharge() {
        return discountCharge;
    }
}
