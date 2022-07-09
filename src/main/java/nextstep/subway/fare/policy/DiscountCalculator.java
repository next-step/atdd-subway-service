package nextstep.subway.fare.policy;

public interface DiscountCalculator {
    int calculate(int totalFare, int age);
}
