package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.function.Function;

public class AgeDiscountFare {
    public static int calculate(int fare, int age) {
        DiscountPolicy policy = Arrays.stream(DiscountPolicy.values())
                .filter(it -> it.isRange(age))
                .findFirst()
                .orElse(DiscountPolicy.NONE);
        return policy.getDiscountFare(fare);
    }

    private enum DiscountPolicy {
        NONE(0, 6, fare -> fare),
        CHILDREN(6, 13, fare -> calculate(fare, 0.5)),
        YOUTH(13, 19, fare -> calculate(fare, 0.2)),
        ADULT(19, Integer.MAX_VALUE, fare -> fare)
        ;

        private int minAge;
        private int maxAge;
        private Function<Integer, Integer> expression;

        DiscountPolicy(int minAge, int maxAge, Function<Integer, Integer> expression) {
            this.minAge = minAge;
            this.maxAge = maxAge;
            this.expression = expression;
        }

        public int getDiscountFare(int fare) {
            return expression.apply(fare);
        }

        public boolean isRange(int age) {
            return minAge <= age && age < maxAge;
        }

        private static int calculate(int fare, double discountRate) {
            return (int)((fare - 350) * (1 - discountRate));
        }
    }
}
