package nextstep.subway.path.domain.discount;

public class AdolescentDiscountPolicy implements DiscountPolicy {

    private static final int DEDUCTIBLE_FARE = 350;
    private static final double DISCOUNT_RETE = 0.2;
    private static final int FREE_FARE = 0;

    @Override
    public int getDiscountFare(int fare) {
        fare -= DEDUCTIBLE_FARE;
        if (fare <= FREE_FARE) {
            return FREE_FARE;
        }
        return calculateDiscountFare(fare, DISCOUNT_RETE);
    }
}
