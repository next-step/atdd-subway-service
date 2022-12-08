package nextstep.subway.domain;


public class SubwayFareCalculator implements FareCalculator {

    private static final int MIN_LINE_FARE = 0;
    private static final int MIN_ADULT_AGE = 19;

    private int lineFare;
    private int age;

    public SubwayFareCalculator() {
        this.lineFare = MIN_LINE_FARE;
        this.age = MIN_ADULT_AGE;
    }

    private SubwayFareCalculator(int lineFare, int age) {
        this.lineFare = lineFare;
        this.age = age;
    }

    public static SubwayFareCalculator from(int lineFare) {
        return new SubwayFareCalculator(lineFare, MIN_ADULT_AGE);
    }

    public static SubwayFareCalculator of(int lineFare, int age) {
        return new SubwayFareCalculator(lineFare, age);
    }

    @Override
    public int calculate(int distance) {
        checkDistanceNotNegative(distance);

        int fare = calculateWithDistance(distance);
        fare += lineFare;

        AgeFarePolicy policy = AgeFarePolicy.findByAge(age);
        return policy.discount(fare);
    }

}
