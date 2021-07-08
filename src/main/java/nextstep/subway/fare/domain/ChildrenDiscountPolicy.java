package nextstep.subway.fare.domain;

public class ChildrenDiscountPolicy implements DiscountPolicy {
    @Override
    public long discount(final long fare) {
        return (long)((fare - 350) * 0.5);
    }
}
