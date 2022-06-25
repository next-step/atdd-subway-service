package nextstep.subway.fare.policy;

public interface DiscountPolicy {
    int discount(int fare);

    default int atLeastBasicFare(int calculateFare, int basicFare) {
        return Math.max(calculateFare, basicFare);
    }
}
