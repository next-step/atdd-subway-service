package nextstep.subway.path.domain;

public enum AgeType {
    CHILD(350, 0.5),
    TEENAGER(350, 0.2),
    DEFAULT(0, 0);

    private final int deductedPrice;
    private final double discountRate;

    AgeType(int deductedPrice, double discountRate) {
        this.deductedPrice = deductedPrice;
        this.discountRate = discountRate;
    }

    public static AgeType findType(Integer age) {
        if (age == null) {
            return DEFAULT;
        }

        if (6 <= age && age < 13) {
            return CHILD;
        }

        if (13 <= age && age < 19) {
            return TEENAGER;
        }

        return DEFAULT;
    }

    public int getDiscountPrice(int price) {
        return (int) ((price - this.deductedPrice) * (1 - this.discountRate));
    }
}
