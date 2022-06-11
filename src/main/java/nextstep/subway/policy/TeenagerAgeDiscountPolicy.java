package nextstep.subway.policy;

public class TeenagerAgeDiscountPolicy implements DiscountPolicy{
    @Override
    public int discountFare(int fare) {
        validateDiscountFare(fare);
        return (int) ((fare - BASIC_DISCOUNT_FARE) * 0.8);
    }
}
