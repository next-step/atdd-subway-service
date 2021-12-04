package nextstep.subway.path.domain;

public class FareCalculator {

    private final Path path;
    private final int age;

    public FareCalculator(Path path, int age) {
        this.path = path;
        this.age = age;
    }

    public int calculate() {
        int fare = fareByDistance(path.getDistance()) +
                path.getMaxAddFare();
        return findAgePolicy().calculateDiscountedFare(fare);
    }

    private int fareByDistance(int distance) {
        return DistancePolicy.BASE_FARE +
                DistancePolicy.MEDIUM.calculateFare(distance) +
                DistancePolicy.LONG.calculateFare(distance);
    }

    private AgePolicy findAgePolicy() {
        return AgePolicy.from(age);
    }
}
