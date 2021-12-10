package nextstep.subway.path.domain.discount;

public class AdolescentDiscountPolicy implements DiscountPolicy {

    private static final int DEDUCTIBLE_FEE = 350;
    private static final double DISCOUNT_RETE = 0.2;
    private static final int FREE_FEE = 0;

    @Override
    public int getDiscountFee(int fee) {
        fee -= DEDUCTIBLE_FEE;
        if (fee <= FREE_FEE) {
            return FREE_FEE;
        }
        return calculateDiscountFee(fee, DISCOUNT_RETE);
    }
}
