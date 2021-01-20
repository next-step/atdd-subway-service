package nextstep.subway.path.domain;

public class FeeCalculator {

    public static final int ZERO = 0;
    public static final int DEFAULT_FEE = 1250;
    public static final int MAX_EXTRA_FEE_WITHIN_50 = 800;
    public static final int EXTRA_CHARGE = 100;
    public static final int DEFAULT_MAX_DISTANCE = 10;
    public static final int SECOND_MAX_DISTANCE = 50;
    public static final int FIRST_EXTRA_CHARGE_UNIT = 5;
    public static final int SECOND_EXTRA_CHARGE_UNIT = 8;

    public static Integer calculateFee(int distance) {
        int fee = ZERO;

        if(distance <= DEFAULT_MAX_DISTANCE) {
            fee = DEFAULT_FEE;
        }

        if(distance > DEFAULT_MAX_DISTANCE && distance <= SECOND_MAX_DISTANCE) {
            int extraArea = distance - DEFAULT_MAX_DISTANCE;
            fee = DEFAULT_FEE
                    + (int) ((Math.ceil((extraArea - 1) / FIRST_EXTRA_CHARGE_UNIT) + 1) * EXTRA_CHARGE);
        }

        if(distance > SECOND_MAX_DISTANCE) {
            int extraArea = distance - SECOND_MAX_DISTANCE;
            fee = DEFAULT_FEE
                    + MAX_EXTRA_FEE_WITHIN_50
                    + (int) ((Math.ceil((extraArea - 1) / SECOND_EXTRA_CHARGE_UNIT) + 1) * EXTRA_CHARGE);
        }

        return fee;
    }
}
