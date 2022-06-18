package nextstep.subway.auth.domain;

public class AdultDiscountPolicy implements DiscountPolicy {

    @Override
    public int discount(int fare) {
        return fare;
    }
}
