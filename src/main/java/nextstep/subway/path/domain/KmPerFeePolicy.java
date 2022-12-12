package nextstep.subway.path.domain;

public class KmPerFeePolicy implements FeeStrategy {
    private final static int DEFAULT_FEE = 1250;
    private final static int ADD_PER_KM_FEE = 100;
    private final static int MIN_KM_LIMIT = 10;
    private final static int MAX_KM_LIMIT = 50;
    private final static int MAX_KM_PER = 8;
    private final static int MIN_KM_PER = 5;
    private static final int CORRECTION_VALUE = 1;


    @Override
    public int calculate(int distance) {
        int fee = DEFAULT_FEE;

        if (distance <= MIN_KM_LIMIT) {
            return fee;
        }

        if (distance > MAX_KM_LIMIT) {
            fee += calculateFee((MAX_KM_LIMIT - MIN_KM_LIMIT), MIN_KM_PER);
            fee += calculateFee((distance - MAX_KM_LIMIT), MAX_KM_PER);
            return fee;
        }

        return fee + calculateFee(distance - MIN_KM_LIMIT, MIN_KM_PER);
    }

    private int calculateFee(int distance, int perKm) {
        return (int) ((Math.ceil((distance - CORRECTION_VALUE) / perKm) + CORRECTION_VALUE) * ADD_PER_KM_FEE);
    }
}
