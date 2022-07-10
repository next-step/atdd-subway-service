package nextstep.subway.charge.domain;

public class DistancePolicy {
    private static final int START_DISTANCE_OF_FIRST_OVER_CHARGE = 10;
    private static final int START_DISTANCE_OF_SECOND_OVER_CHARGE = 50;
    private static final int UNIT_DISTANCE_OF_FIRST_OVER_CHARGE = 5;
    private static final int UNIT_DISTANCE_OF_SECOND_OVER_CHARGE = 8;
    private static final int ADDITIONAL_CHARGE = 100;

    private static final String ERROR_MESSAGE_TOTAL_DISTANCE_LESS_THAN_ZERO = "총 거리가 0 이하입니다.";
    private final int overCharge;

    public DistancePolicy(Integer totalDistance) {
        validateTotalDistance(totalDistance);

        overCharge = calculateTotalOverCharge(totalDistance);
    }

    private void validateTotalDistance(Integer totalDistance) {
        if (totalDistance <= 0) {
            throw new IllegalArgumentException(ERROR_MESSAGE_TOTAL_DISTANCE_LESS_THAN_ZERO);
        }
    }

    private int calculateTotalOverCharge(Integer totalDistance) {
        return calculateFirstOverCharge(totalDistance) + calculateSecondOverCharge(totalDistance);
    }

    private int calculateFirstOverCharge(Integer totalDistance) {
        if (totalDistance <= START_DISTANCE_OF_FIRST_OVER_CHARGE) {
            return 0;
        }

        double distance = totalDistance;
        if (distance > START_DISTANCE_OF_SECOND_OVER_CHARGE) {
            distance = START_DISTANCE_OF_SECOND_OVER_CHARGE;
        }

        return (int) Math.ceil(
                (distance - START_DISTANCE_OF_FIRST_OVER_CHARGE) / (double) UNIT_DISTANCE_OF_FIRST_OVER_CHARGE)
                * ADDITIONAL_CHARGE;
    }

    private int calculateSecondOverCharge(Integer totalDistance) {
        if (totalDistance <= START_DISTANCE_OF_SECOND_OVER_CHARGE) {
            return 0;
        }

        return (int) Math.ceil(
                (totalDistance - START_DISTANCE_OF_SECOND_OVER_CHARGE) / (double) UNIT_DISTANCE_OF_SECOND_OVER_CHARGE)
                * ADDITIONAL_CHARGE;
    }

    public void applyPolicy(Charge charge) {
        charge.plus(overCharge);
    }
}
