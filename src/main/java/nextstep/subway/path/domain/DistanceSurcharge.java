package nextstep.subway.path.domain;

import static java.lang.Math.floor;

public class DistanceSurcharge extends Charge {
    private static final int BOUNDARY_1ST_SURCHARGE = 10;
    private static final int BOUNDARY_2ND_SURCHARGE = 50;

    private DistanceSurcharge(int value) {
        super(value);
    }

    public static DistanceSurcharge from(int distance) {
        if (is1stSurcharge(distance)) {
            return new DistanceSurcharge(surcharge1st(distance));
        }
        if (is2ndSurcharge(distance)) {
            return new DistanceSurcharge(surcharge2nd(distance));
        }
        return new DistanceSurcharge(0);
    }

    private static boolean is1stSurcharge(int distance) {
        return distance > BOUNDARY_1ST_SURCHARGE && distance <= BOUNDARY_2ND_SURCHARGE;
    }

    private static int surcharge1st(int distance) {
        int over = distance - BOUNDARY_1ST_SURCHARGE;
        return (int) ((floor((over - 1) / 5.0) + 1) * 100);
    }

    private static boolean is2ndSurcharge(int distance) {
        return distance > BOUNDARY_2ND_SURCHARGE;
    }

    private static int surcharge2nd(int distance) {
        int over = distance - BOUNDARY_2ND_SURCHARGE;
        return surcharge1st(BOUNDARY_2ND_SURCHARGE) + (int) ((floor((over - 1) / 8.0) + 1) * 100);
    }
}
