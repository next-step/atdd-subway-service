package nextstep.subway.policy;

public class FreeDiscountPolicy implements DiscountPolicy {
    @Override
    public int discountFare(int fare) {
        return 0;
    }
}
