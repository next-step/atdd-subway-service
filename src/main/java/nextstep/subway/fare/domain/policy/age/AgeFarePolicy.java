package nextstep.subway.fare.domain.policy.age;

public interface AgeFarePolicy {
    double discountRate = 0;
    int defaultFare = 1250;
    int discountFare = 0;

    int calculate();
    boolean isAge(int age);

    double getDiscountRate();

}
