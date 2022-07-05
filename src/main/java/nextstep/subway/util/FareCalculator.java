package nextstep.subway.util;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.domain.User;
import nextstep.subway.line.domain.Fare;

public class FareCalculator {

    private static final long EXTRA_FARE = 100;
    private static final long DEFAULT_FARE = 1_250;
    private static final long DEFAULT_FARE_DISTANCE_LIMIT = 10;
    private static final long SHORT_EXTRA_FARE_DISTANCE_LIMIT = 50;
    private static final long SHORT_EXTRA_FARE_CHARGE_DISTANCE = 5;
    private static final long LONG_EXTRA_FARE_CHARGE_DISTANCE = 8;


    public static Fare calculateFare(long distance, long fare) {
        if (distance <= DEFAULT_FARE_DISTANCE_LIMIT) {
            return Fare.of(DEFAULT_FARE + fare);
        }
        if (distance <= SHORT_EXTRA_FARE_DISTANCE_LIMIT) {
            return Fare.of(shortExtraDistanceCalculate(distance) + DEFAULT_FARE + fare);
        }
        return Fare.of(longExtraDistanceCalculate(distance) + DEFAULT_FARE + fare);
    }


    public static Fare calculateFareWithMemberType(User user, long fare) {
        if (user.isGuest()) {
            return Fare.of(fare);
        }
        return Fare.of((long) ((fare - ((LoginMember) user).findMemberType().getDeductedAmount()) * (1 - ((LoginMember) user).findMemberType().getDiscountRate())));
    }


    public static long shortExtraDistanceCalculate(long distance) {
        return extraDistanceCalculate(distance - DEFAULT_FARE_DISTANCE_LIMIT, SHORT_EXTRA_FARE_CHARGE_DISTANCE) * EXTRA_FARE;
    }

    public static long longExtraDistanceCalculate(long distance) {
        return shortExtraDistanceCalculate(SHORT_EXTRA_FARE_DISTANCE_LIMIT) + extraDistanceCalculate(distance - SHORT_EXTRA_FARE_DISTANCE_LIMIT, LONG_EXTRA_FARE_CHARGE_DISTANCE) * EXTRA_FARE;
    }

    public static long extraDistanceCalculate(long distance, long extraChargeDistance) {
        if (distance % extraChargeDistance == 0) {
            return distance / extraChargeDistance;
        }
        return distance / extraChargeDistance + 1;
    }

}
