package nextstep.subway.policy;

public class NonDiscountPolicy implements DiscountPolicy {
    @Override
    public int discountFare(int fare) {
        return fare;
    }
}
