package nextstep.subway.path.infra;

import nextstep.subway.common.Money;
import nextstep.subway.path.domain.Distance;
import nextstep.subway.path.domain.DistanceFee;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.function.Function;

@Component
public class DefaultDistanceFee implements DistanceFee {
    public static final int BASIC_FEE = 1250;
    public static final int ADD_FEE = 100;

    private static final int BASIC_DISTANCE = 10;
    private static final int MEDIUM_INTERVAL = 5;
    private static final int LARGE_INTERVAL = 8;

    @Override
    public Money settle(final Distance distance) {
        int distanceValue = distance.getValue();
        DistanceFeeOperator distanceFeeOperator = DistanceFeeOperator.findByDistance(distanceValue);
        int settle = distanceFeeOperator.settle(distanceValue);
        return Money.valueOf(settle);
    }

    private enum DistanceFeeOperator {
        BASIC(10, distance -> BASIC_FEE),
        MEDIUM(50, distance -> calculate(distance, MEDIUM_INTERVAL)),
        LARGE(0, distance -> calculate(distance, LARGE_INTERVAL));

        private final int standard;

        private final Function<Integer, Integer> function;

        DistanceFeeOperator(final int standard, final Function<Integer, Integer> function) {
            this.standard = standard;
            this.function = function;
        }

        private static int calculate(final int distance, final int interval) {
            int additionalDistance = distance - BASIC_DISTANCE;
            return BASIC_FEE + (ADD_FEE * (additionalDistance / interval));
        }

        public static DistanceFeeOperator findByDistance(final int distance) {
            return Arrays.stream(DistanceFeeOperator.values())
                    .filter(distanceFeeType -> distanceFeeType.isStandard(distance))
                    .findFirst()
                    .orElse(LARGE);
        }

        private boolean isStandard(final int distance) {
            return standard >= distance;
        }

        public int settle(final int distance) {
            return function.apply(distance);
        }
    }
}
