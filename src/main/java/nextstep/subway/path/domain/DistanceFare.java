package nextstep.subway.path.domain;

import java.util.function.Function;

public class DistanceFare {
    public static int calculate(int distance) {
        DistanceFarePolicy[] polices = DistanceFarePolicy.values();

        int totalFee = 0;
        for(DistanceFarePolicy policy: polices) {
            if(distance > policy.excessDistance) {
                totalFee += policy.getFare(distance - policy.excessDistance);
                distance = policy.excessDistance;
            }
        }

        return totalFee;
    }

    private enum DistanceFarePolicy {
        FEE50(50, distance -> calculate(distance, 8, 100)),
        FEE10(10, distance -> calculate(distance, 5, 100)),
        BASE(0, distance -> 1250)
        ;

        private int excessDistance;
        private  Function<Integer, Integer> expression;

        DistanceFarePolicy(int excessDistance, Function<Integer, Integer> expression) {
            this.excessDistance = excessDistance;
            this.expression = expression;
        }

        public int getFare(int distance) {
            return expression.apply(distance);
        }

        private static int calculate(int distance, int unit, int unitFee) {
            return (int) ((Math.floor((distance - 1) / unit) + 1) * unitFee);
        }
    }
}
