package nextstep.subway.path.domain.discount;

public class KidDiscountPolicy implements DiscountPolicy {

    private static final int DEDUCTIBLE_FEE = 350;
    private static final double DISCOUNT_RETE = 0.5;
    private static final int FREE_FEE = 0;

    @Override
    public int discount(int fee) {
        fee -= DEDUCTIBLE_FEE;
        if (fee <= FREE_FEE) {
            return FREE_FEE;
        }
        return calculateDiscountFee(fee, DISCOUNT_RETE);
    }
}
