package nextstep.subway.path.domain;

import static nextstep.subway.path.domain.Fare.ADDITIONAL_FARE;
import static nextstep.subway.path.domain.Fare.MIDDLE_SECTOR_CHARGE_DISTANCE;
import static nextstep.subway.path.domain.Fare.MIDDLE_DISTANCE;
import static nextstep.subway.path.domain.Fare.HIGH_SECTOR_CHARGE_DISTANCE;
import static nextstep.subway.path.domain.Fare.HIGH_DISTANCE;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum FareDistance {
    MIDDLE(distance -> distance > MIDDLE_DISTANCE,
            distance -> calSectorFare(Math.min(distance - MIDDLE_DISTANCE, HIGH_DISTANCE - MIDDLE_DISTANCE),
                    MIDDLE_SECTOR_CHARGE_DISTANCE)),
    HIGH(distance -> distance > HIGH_DISTANCE,
            distance -> calSectorFare(distance - HIGH_DISTANCE, HIGH_SECTOR_CHARGE_DISTANCE));

    private final Predicate<Double> range;
    private final Function<Double, Integer> formula;

    FareDistance(Predicate<Double> range, Function<Double, Integer> formula) {
        this.range = range;
        this.formula = formula;
    }

    public static int calAdditionalFare(double distance) {
        return Arrays.stream(FareDistance.values())
                .filter(i -> i.range.test(distance))
                .map(i -> i.formula.apply(distance))
                .reduce(0, Integer::sum);
    }


    private static int calSectorFare(double distance, int chargingDistance) {
        return (int) ((Math.ceil((distance - 1) / chargingDistance) + 1) * ADDITIONAL_FARE);
    }


}
