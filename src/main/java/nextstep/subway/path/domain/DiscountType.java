package nextstep.subway.path.domain;

public enum DiscountType {

    NONE(0),
    TEENAGER(0.2),
    CHILDREN(0.5);

    private double discountPercent;

    DiscountType(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }
}
