package nextstep.subway.fare.domain.policy.age;

public interface AgeFarePolicy {

    int calculate();

    boolean includeAge(int age);

    int getDiscountRate();

}
