package nextstep.subway.path.policy;

public class BasicAgeDiscountPolicy implements AgeDiscountPolicy {
    private static final int BASIC_FARE = 1250;

    @Override
    public int discount(int fare) {
        return Math.max(fare, BASIC_FARE);
    }
}
