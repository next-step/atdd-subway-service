package nextstep.subway.fare.domain;

public class NoneDiscountPolicy implements DiscountPolicy {
    @Override
    public long discount(final long fare) {
        return fare;
    }
}
