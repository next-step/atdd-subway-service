package nextstep.subway.path.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static nextstep.subway.path.domain.enums.FeeDistanceRangeType.*;

public class KmPerFeePolicy implements FeeStrategy {

    @Override
    public int calculate(int distance) {
        int fee = DEFAULT_FEE.getKmPerFee();

        if (distance <= MIN_KM_LIMIT.getKmPerFee()) {
            return fee;
        }

        if (distance > MAX_KM_LIMIT.getKmPerFee()) {
            fee += calculateFee((MAX_KM_LIMIT.getKmPerFee() - MIN_KM_LIMIT.getKmPerFee()), MIN_KM_PER.getKmPerFee());
            fee += calculateFee((distance - MAX_KM_LIMIT.getKmPerFee()), MAX_KM_PER.getKmPerFee());
            return fee;
        }

        return fee + calculateFee(distance - MIN_KM_LIMIT.getKmPerFee(), MIN_KM_PER.getKmPerFee());
    }

    private int calculateFee(int distance, int perKm) {
        int divideResult = new BigDecimal(distance - CORRECTION_VALUE.getKmPerFee()).divide(new BigDecimal(perKm)).intValue();

        return (divideResult + CORRECTION_VALUE.getKmPerFee()) * ADD_PER_KM_FEE.getKmPerFee();
    }
}
