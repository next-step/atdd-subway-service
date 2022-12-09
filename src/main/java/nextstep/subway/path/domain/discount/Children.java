package nextstep.subway.path.domain.discount;

public class Children extends AgeDiscount {

    public static final int CHILDREN_DEDUCTION_FARE = 350;
    public static final double CHILDREN_DISCOUNT_RATIO = 0.5;

    public Children(int fare) {
        super(fare);
    }

    @Override
    public int discount() {
        return (int) ((getFare() - CHILDREN_DEDUCTION_FARE) * CHILDREN_DISCOUNT_RATIO);
    }
}
