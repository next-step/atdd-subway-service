package nextstep.subway.path.domain.fare;

public interface DiscountOfAgeCalculator {
    boolean isTarget(int age);
    int discount(int totalFare);
}
