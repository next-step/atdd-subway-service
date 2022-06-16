package nextstep.subway.path.domain.policy;

public class NonDiscountPolicy implements DiscountPolicy{
    @Override
    public int discount(int fare) {
        return fare;
    }
}
