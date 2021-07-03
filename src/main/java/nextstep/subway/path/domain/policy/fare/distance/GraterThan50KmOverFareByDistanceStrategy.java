package nextstep.subway.path.domain.policy.fare.distance;

import nextstep.subway.path.domain.ShortestDistance;
import org.springframework.stereotype.Component;

public class GraterThan50KmOverFareByDistanceStrategy implements OverFareByDistanceStrategy {
    public static final int PER_KM = 8;
    public static final int OVER_FARE_PER_KM = 100;
    public static final int MIN_DISTANCE = 51;

    @Override
    public int calculateOverFare(ShortestDistance distance) {
        int overDistance = distance.value() - GraterThan10KmNotMoreThan50KmOverFareByDistanceStrategy.MAX_DISTANCE;
        return calculateOverFare(GraterThan10KmNotMoreThan50KmOverFareByDistanceStrategy.MAX_DISTANCE -
                        NotMoreThan10KmOverFareByDistanceStrategy.MAX_DISTANCE,
                GraterThan10KmNotMoreThan50KmOverFareByDistanceStrategy.PER_KM,
                GraterThan10KmNotMoreThan50KmOverFareByDistanceStrategy.OVER_FATE_PER_KM) +
                calculateOverFare(overDistance, PER_KM, OVER_FARE_PER_KM);
    }

    @Override
    public boolean isAvailable(ShortestDistance distance) {
        return distance.isGraterThan(MIN_DISTANCE - 1);
    }

    public int calculateOverFare(int distance, int perKm, int overFarePerKm) {
        return (int) ((Math.ceil((distance - 1) / perKm) + 1) * overFarePerKm);
    }
}
