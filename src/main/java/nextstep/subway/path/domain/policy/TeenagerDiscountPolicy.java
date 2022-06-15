package nextstep.subway.path.domain.policy;

public class TeenagerDiscountPolicy implements DiscountPolicy {

    private static final int BASE_FARE = 350;

    @Override
    public int discount(int fare) {
        if (fare <= BASE_FARE) {
            return 0;
        }
        return (int) Math.ceil((fare - 350) * 0.8);
    }
}
