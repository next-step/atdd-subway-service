package nextstep.subway.path.domain.policy;

public class ChargeFareByDistancePolicy implements ChargeFarePolicy {
    private static final int DEFAULT_FARE = 1_250;
    private static final int DEFAULT_FARE_SECTION_DISTANCE = 10;
    private static final int EXCESS_FARE_SECTION_DISTANCE = 50;
    private static final int FARE_PER_UNIT_DISTANCE = 100;
    private static final int CORRECTION_VALUE = 1;
    private static final int UNIT_MID_DISTANCE = 5;
    private static final int UNIT_LONG_DISTANCE = 8;

    @Override
    public int charge(int distance) {
        int fareByDistance = DEFAULT_FARE;

        if (distance > DEFAULT_FARE_SECTION_DISTANCE && distance <= EXCESS_FARE_SECTION_DISTANCE) {
            fareByDistance += calculateOverFare(distance - DEFAULT_FARE_SECTION_DISTANCE, UNIT_MID_DISTANCE);
        }

        if (distance > EXCESS_FARE_SECTION_DISTANCE) {
            fareByDistance += calculateOverFare(EXCESS_FARE_SECTION_DISTANCE - DEFAULT_FARE_SECTION_DISTANCE, UNIT_MID_DISTANCE);
            fareByDistance += calculateOverFare(distance - EXCESS_FARE_SECTION_DISTANCE, UNIT_LONG_DISTANCE);
        }

        return fareByDistance;
    }

    private int calculateOverFare(int overDistance, int unitDistance) {
        return (int) ((Math.ceil((overDistance - CORRECTION_VALUE) / unitDistance) + CORRECTION_VALUE) * FARE_PER_UNIT_DISTANCE);
    }
}
