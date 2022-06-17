package nextstep.subway.auth.domain;

public class ChildDiscountPolicy implements DiscountPolicy {

    @Override
    public int discount(int fare) {
        return (int)((fare - 350) * 0.5);
    }
}
