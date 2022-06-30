package nextstep.subway.path.domain;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Charge {
    private static final List<Integer> YOUTH_AGE_RANGE = IntStream.range(13, 19).boxed().collect(Collectors.toList());
    private static final List<Integer> KID_AGE_RANGE = IntStream.range(6, 13).boxed().collect(Collectors.toList());

    private static final int START_DISTANCE_OF_FIRST_OVER_CHARGE = 10;
    private static final int START_DISTANCE_OF_SECOND_OVER_CHARGE = 50;
    private static final int UNIT_DISTANCE_OF_FIRST_OVER_CHARGE = 5;
    private static final int UNIT_DISTANCE_OF_SECOND_OVER_CHARGE = 8;
    private static final int ADDITIONAL_CHARGE = 100;

    private static final int DEFAULT_CHARGE = 1250;

    private static final String ERROR_MESSAGE_TOTAL_DISTANCE_LESS_THAN_ZERO = "총 거리가 0 이하입니다.";
    private static final String ERROR_MESSAGE_MAX_EXTRA_CHARGE_LESS_THAN_ZERO = "최고 노선 추가 운임료가 0 미만입니다.";
    private static final String ERROR_MESSAGE_AGE_LESS_THAN_ZERO = "나이가 0살 이하입니다.";

    private final int totalDistance;
    private final int maxExtraCharge;
    private AgeType ageType;

    public Charge(Integer totalDistance, Integer maxExtraCharge) {
        validate(totalDistance, maxExtraCharge);

        this.totalDistance = totalDistance;
        this.maxExtraCharge = maxExtraCharge;
        this.ageType = AgeType.NORMAL;
    }

    private void validate(Integer totalDistance, Integer maxExtraCharge) {
        validateTotalDistance(totalDistance);
        validateMaxExtraCharge(maxExtraCharge);
    }

    private void validateTotalDistance(Integer totalDistance) {
        if (totalDistance <= 0) {
            throw new IllegalArgumentException(ERROR_MESSAGE_TOTAL_DISTANCE_LESS_THAN_ZERO);
        }
    }

    private void validateMaxExtraCharge(Integer maxExtraCharge) {
        if (maxExtraCharge < 0) {
            throw new IllegalArgumentException(ERROR_MESSAGE_MAX_EXTRA_CHARGE_LESS_THAN_ZERO);
        }
    }

    public void updateAgeType(Integer age) {
        validateAge(age);

        this.ageType = getAgeType(age);
    }

    private void validateAge(Integer age) {
        if (age <= 0) {
            throw new IllegalArgumentException(ERROR_MESSAGE_AGE_LESS_THAN_ZERO);
        }
    }

    private AgeType getAgeType(Integer age) {
        if (YOUTH_AGE_RANGE.contains(age)) {
            return AgeType.YOUTH;
        }

        if (KID_AGE_RANGE.contains(age)) {
            return AgeType.KID;
        }

        return AgeType.NORMAL;
    }

    public Integer getFare() {
        int charge = DEFAULT_CHARGE + maxExtraCharge;

        charge += calculateFirstOverCharge();
        charge += calculateSecondOverCharge();

        return calculateByAgeType(charge);
    }

    private int calculateFirstOverCharge() {
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

    private int calculateSecondOverCharge() {
        if (totalDistance <= START_DISTANCE_OF_SECOND_OVER_CHARGE) {
            return 0;
        }

        return (int) Math.ceil(
                (totalDistance - START_DISTANCE_OF_SECOND_OVER_CHARGE) / (double) UNIT_DISTANCE_OF_SECOND_OVER_CHARGE)
                * ADDITIONAL_CHARGE;
    }

    private int calculateByAgeType(int charge) {
        return (int) ((charge - ageType.getDiscountCharge()) * ageType.getChargeRatio());
    }

    private enum AgeType {
        NORMAL(1.0, 0),
        YOUTH(0.8, 350),
        KID(0.5, 350);

        private final double chargeRatio;
        private final int discountCharge;

        AgeType(double chargeRatio, int discountCharge) {
            this.chargeRatio = chargeRatio;
            this.discountCharge = discountCharge;
        }

        private double getChargeRatio() {
            return chargeRatio;
        }

        private int getDiscountCharge() {
            return discountCharge;
        }
    }
}
