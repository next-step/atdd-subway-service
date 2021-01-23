package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;

public class FareCalculator {
    public static final int ZERO = 0;
    public static final int DEFAULT_FARE = 1250;
    public static final int MAX_EXTRA_FARE_WITHIN_50 = 800;
    public static final int EXTRA_CHARGE = 100;
    public static final int DEFAULT_MAX_DISTANCE = 10;
    public static final int SECOND_MAX_DISTANCE = 50;
    public static final int FIRST_EXTRA_CHARGE_UNIT = 5;
    public static final int SECOND_EXTRA_CHARGE_UNIT = 8;
    public static final int ONE = 1;
    public static final double YOUTH_RATE = 0.8;
    public static final double CHILD_RATE = 0.5;
    public static final int EXCLUDE_DISCOUN_FARE = 350;

    public static Integer calculateFare(int distance) {
        int fare = ZERO;

        if (distance <= DEFAULT_MAX_DISTANCE) {
            fare = DEFAULT_FARE;
        }

        if (distance > DEFAULT_MAX_DISTANCE && distance <= SECOND_MAX_DISTANCE) {
            int extraArea = distance - DEFAULT_MAX_DISTANCE;
            fare = DEFAULT_FARE
                    + (int) ((Math.ceil((extraArea - ONE) / FIRST_EXTRA_CHARGE_UNIT) + ONE) * EXTRA_CHARGE);
        }

        if (distance > SECOND_MAX_DISTANCE) {
            int extraArea = distance - SECOND_MAX_DISTANCE;
            fare = DEFAULT_FARE
                    + MAX_EXTRA_FARE_WITHIN_50
                    + (int) ((Math.ceil((extraArea - ONE) / SECOND_EXTRA_CHARGE_UNIT) + ONE) * EXTRA_CHARGE);
        }

        return fare;
    }

    public static Integer calculateFare(Integer lineExtraFare, Integer distance, LoginMember member) {
        int defaultFare = calculateFare(distance) + lineExtraFare;
        if (member.youth()) {
            return (int) ((defaultFare - EXCLUDE_DISCOUN_FARE) * YOUTH_RATE);
        }
        if (member.child()) {
            return (int) ((defaultFare - EXCLUDE_DISCOUN_FARE) * CHILD_RATE);
        }
        return defaultFare;
    }
}
