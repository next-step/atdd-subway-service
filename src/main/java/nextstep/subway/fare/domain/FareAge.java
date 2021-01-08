package nextstep.subway.fare.domain;

import java.util.Arrays;
import java.util.Comparator;

public class FareAge {
    private enum Policy {
        CHILD(6, 350, 0.5),
        TEENAGER(13, 350, 0.2),
        ADULT(19, 0, 0);

        private static final int ZERO_AMOUNT = 0;
        private final int age;
        private final int basicAmount;
        private final double discount;

        Policy(int age, int basicAmount, double discount) {
            this.age = age;
            this.basicAmount = basicAmount;
            this.discount = discount;
        }

        public int getAge() {
            return age;
        }

        public static int calculateDiscountAmount(int age, int fare) {
            return Arrays.stream(values())
                    .sorted(Comparator.comparing(Policy::getAge).reversed())
                    .filter(agePolicy -> agePolicy.age <= age)
                    .findFirst()
                    .map(fareDistance -> discountAmount(fare, fareDistance))
                    .orElse(ZERO_AMOUNT);
        }

        private static int discountAmount(int fare, Policy policy) {
            return fare - (int) ((fare - policy.basicAmount) * (1 - policy.discount));
        }
    }

    private final int age;

    public FareAge(int age) {
        this.age = age;
    }

    public int getAmount(int fare) {
        return Policy.calculateDiscountAmount(this.age, fare);
    }
}
