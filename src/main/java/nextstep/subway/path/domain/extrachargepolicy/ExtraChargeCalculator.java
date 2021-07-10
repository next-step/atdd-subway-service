package nextstep.subway.path.domain.extrachargepolicy;

public class ExtraChargeCalculator {

    public static final int DEFAULT_FARE = 1250;

    public static ExtraChargePolicy getExtraChargePolicy(int distance) {
        if (distance <= 10) {
            return () -> 0;
        }

        if (distance < 50) {
            return new ExtraChargePer5KmPolicy(distance);
        }

        return new ExtraChargePer8KmPolicy(distance);
    }
}
