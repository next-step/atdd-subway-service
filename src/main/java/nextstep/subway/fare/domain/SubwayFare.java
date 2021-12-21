package nextstep.subway.fare.domain;

public class SubwayFare {

    private final int fare;
    private final DiscountPolicy discountPolicy;

    private SubwayFare(int fare, DiscountPolicy discountPolicy) {
        this.fare = fare;
        this.discountPolicy = discountPolicy;
    }

    public static SubwayFare of(int fare, DiscountPolicy discountPolicy) {
        return new SubwayFare(fare, discountPolicy);
    }

    public int getFare() {
        return fare;
    }

    public int calculateDiscountFare() {
        return discountPolicy.calculateDiscountFare(fare);
    }
}
