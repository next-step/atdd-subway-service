package nextstep.subway.path.domain.fare;

public class DefaultAgePolicy implements DiscountOfAgeCalculator {

    @Override
    public boolean isTarget(int age) {
        return false;
    }

    @Override
    public int discount(int totalFare) {
        return totalFare;
    }
}
