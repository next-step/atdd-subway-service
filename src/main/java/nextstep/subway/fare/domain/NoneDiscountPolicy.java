package nextstep.subway.fare.domain;

public class NoneDiscountPolicy implements DiscountPolicy {

    @Override
    public int calculateDiscountFare(Integer fare) {
        return fare;
    }
}
