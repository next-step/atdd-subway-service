package nextstep.subway.path.domain;

public class FareDistanceCalculator {

    private static final int INCREMENT_FARE = 100;
    private static final int MIDDLE_CONDITION = 5;
    private static final int LONG_CONDITION = 8;

    public static Fare calculate(int distance) {
        FareDistance fareDistance = FareDistance.findByDistance(distance);

        if (fareDistance == FareDistance.BASIC) {
            return Fare.from(0);
        }

        if (fareDistance == FareDistance.MIDDLE) {
            return Fare.from(calculateOverFare(distance - FareDistance.BASIC.getEnd(), MIDDLE_CONDITION));
        }

        int middleFare = calculateOverFare(FareDistance.MIDDLE.getEnd() - FareDistance.BASIC.getEnd(), MIDDLE_CONDITION);
        int longFare = calculateOverFare(distance - FareDistance.MIDDLE.getEnd(), LONG_CONDITION);
        return Fare.from(middleFare + longFare);
    }

    private static int calculateOverFare(int distance, int condition) {
        return (int) ((Math.ceil((distance - 1) / condition) + 1) * INCREMENT_FARE);
    }

}
