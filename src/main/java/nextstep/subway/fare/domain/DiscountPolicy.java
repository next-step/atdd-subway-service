package nextstep.subway.fare.domain;

public enum DiscountPolicy {
    ADULT(19, 150, 0, 0),
    TEENAGER(13, 19, 350, 0.2),
    CHILD(6, 13, 350, 0.5),
    INFANT(0, 6, 0, 1);

    private int ageStart;
    private final int ageEnd;
    private final int minusFare;
    private final double discountRatio;

    DiscountPolicy(int ageStart, int ageEnd, int minusFare, double discountRatio) {

        this.ageStart = ageStart;
        this.ageEnd = ageEnd;
        this.minusFare = minusFare;
        this.discountRatio = discountRatio;
    }
    
    public int discount(int fare) {
        if (fare < 0) {
            throw new IllegalArgumentException();
        }
        double discounted = (fare - this.minusFare) * (1 - this.discountRatio);
        return (int) discounted;
    }
}
