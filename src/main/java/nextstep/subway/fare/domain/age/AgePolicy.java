package nextstep.subway.fare.domain.age;

public interface AgePolicy {
    int DEFAULT_FARE = 1250;
    int DISCOUNT_FARE = 350;

    int calculate();
}
