package nextstep.subway.path.domain.discount;

public class Teenager extends AgeDiscount {

    public static final int TEENAGER_DEDUCTION_FARE = 350;
    public static final double TEENAGER_DISCOUNT_RATIO = 0.8;

    public Teenager(int fare) {
        super(fare);
    }

    @Override
    public int discount() {
        return (int) ((getFare() - TEENAGER_DEDUCTION_FARE) * TEENAGER_DISCOUNT_RATIO);
    }
}
