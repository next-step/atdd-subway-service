package nextstep.subway.path.policy;

import java.util.Arrays;

public class DefaultAgeDiscountPolicy implements DiscountPolicy {
    private final int age;

    public DefaultAgeDiscountPolicy(int age) {
        this.age = age;
    }

    public int apply(int fare) {
        return AgeSection.discount(fare, age);
    }

    enum AgeSection {
        CHILD (6, 12, 50),
        YOUTH (13, 18, 20),
        ADULT (19, Integer.MAX_VALUE, 0);

        private final static int DEDUCTIBLE = 350;
        private final int startAge;
        private final int endAge;
        private final int discountRate;

        AgeSection(int startAge, int endAge, int discountRate) {
            this.startAge = startAge;
            this.endAge = endAge;
            this.discountRate = discountRate;
        }

        static int discount(int fare, int age) {
            AgeSection ageSection = Arrays.stream(values())
                    .filter(section -> section.isAssigned(age))
                    .findFirst()
                    .orElse(ADULT);
            int discount = (fare - DEDUCTIBLE) * ageSection.discountRate / 100;
            return fare - discount;
        }

        private boolean isAssigned(int age) {
            return startAge <= age && endAge >= age;
        }
    }
}
