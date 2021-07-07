package nextstep.subway.fare.domain;

public class TeenagerDiscountPolicy implements DiscountPolicy {
    @Override
    public long discount(final long fare) {
        return (long)((fare - 350) * 0.8);
    }
}
