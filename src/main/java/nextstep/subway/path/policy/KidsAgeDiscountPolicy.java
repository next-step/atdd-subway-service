package nextstep.subway.path.policy;

public class KidsAgeDiscountPolicy implements AgeDiscountPolicy {
    private static final int BASIC_FARE = 1250;
    private static final int DEDUCTION_FARE = 350;

    @Override
    public int discount(int fare) {
        return (Math.max(calculateFare(fare), calculateFare(BASIC_FARE)));
    }

    private int calculateFare(int fare) {
        return (int) ((fare - DEDUCTION_FARE) * 0.5);
    }
}
