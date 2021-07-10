package nextstep.subway.path.domain.discountpolicy;

public class ChildrenDiscountPolicy implements DiscountPolicy {

    @Override
    public long calculate(long fare) {
        return (long)((fare - 350) * 0.5);
    }
}
