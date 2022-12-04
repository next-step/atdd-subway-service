package nextstep.subway.auth.domain.discount;

public class TeenagerDiscountPolicy implements DiscountPolicy {

    private static final double DEDUCTIBLE_AMOUNT = 350;
    private static final double DISCOUNT_RATE = 0.2;

    @Override
    public int discount(int cost) {
        double discountCost = cost - DEDUCTIBLE_AMOUNT;
        discountCost = discountCost * DISCOUNT_RATE;
        return (int) (cost - discountCost);
    }
}
