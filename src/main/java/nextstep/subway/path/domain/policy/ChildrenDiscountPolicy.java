package nextstep.subway.path.domain.policy;

public class ChildrenDiscountPolicy implements DiscountPolicy {

    public static final int BASE_FARE = 350;
    public static final double CHILDREN_DISCOUNT_RATE = 0.5;

    @Override
    public int discount(int fare) {
        if (fare <= BASE_FARE) {
            return 0;
        }
        return (int) Math.ceil((fare - BASE_FARE) * (1 - CHILDREN_DISCOUNT_RATE));
    }
}
