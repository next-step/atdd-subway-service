package nextstep.subway.auth.domain;

public class YouthDiscountPolicy implements DiscountPolicy {

    @Override
    public int discount(int fare) {
        return (int)((fare - 350) * 0.8);
    }
}
