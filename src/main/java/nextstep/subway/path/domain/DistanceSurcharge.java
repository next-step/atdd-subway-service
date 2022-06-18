package nextstep.subway.path.domain;

public class DistanceSurcharge extends Charge {
    private static final int FIRST_SURCHARGE_DISTANCE = 10;
    private static final int SECOND_SURCHARGE_DISTANCE = 50;

    private DistanceSurcharge(int value) {
        super(value);
    }

    public static DistanceSurcharge from(int distance) {
        if (is10to50(distance)) {
            return new DistanceSurcharge(surcharge10to50(distance));
        }
        if (is50over(distance)) {
            return new DistanceSurcharge(surcharge50over(distance));
        }
        return new DistanceSurcharge(0);
    }

    private static boolean is10to50(int distance) {
        return distance > FIRST_SURCHARGE_DISTANCE && distance <= SECOND_SURCHARGE_DISTANCE;
    }

    private static int surcharge10to50(int distance) {
        int c = (distance - FIRST_SURCHARGE_DISTANCE) / 5;
        return 100 * c;
    }

    private static boolean is50over(int distance) {
        return distance > SECOND_SURCHARGE_DISTANCE;
    }

    private static int surcharge50over(int distance) {
        int d = (distance - SECOND_SURCHARGE_DISTANCE) / 8;
        return surcharge10to50(50) + 100 * d;
    }
}
