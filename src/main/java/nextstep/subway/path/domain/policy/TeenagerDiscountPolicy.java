package nextstep.subway.path.domain.policy;

public class TeenagerDiscountPolicy implements DiscountPolicy {

    private static final int BASE_FARE = 350;
    public static final double TEENAGER_DISCOUNT_RATE = 0.2;

    @Override
    public int discount(int fare) {
        if (fare <= BASE_FARE) {
            return 0;
        }
        return (int) Math.ceil((fare - BASE_FARE) * (1- TEENAGER_DISCOUNT_RATE));
    }
}
