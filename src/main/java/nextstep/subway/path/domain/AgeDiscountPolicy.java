package nextstep.subway.path.domain;

public class AgeDiscountPolicy implements DiscountPolicy {
    private static final int MIN_OF_CHILDREN_AGE = 6;
    private static final int MIN_OF_YOUTH_AGE = 13;
    private static final int MIN_OF_ADULT_AGE = 19;
    private static final int MIN_OF_OLDMAN_AGE = 65;
    private final int age;

    public AgeDiscountPolicy(int age) {
        this.age = age;
    }

    @Override
    public int discount(int sourceFare) {
        if (isPreschoolerOrOldMan()) {
            return 0;
        }

        if (isChild()) {
            return (int) ((sourceFare - 350) * 0.5);
        }

        if (isYouth()) {
            return (int) ((sourceFare - 350) * 0.8);
        }

        return sourceFare;
    }

    private boolean isPreschoolerOrOldMan() {
        return age < MIN_OF_CHILDREN_AGE || age >= MIN_OF_OLDMAN_AGE;
    }

    private boolean isChild() {
        return age >= MIN_OF_CHILDREN_AGE && age < MIN_OF_YOUTH_AGE;
    }

    private boolean isYouth() {
        return age >= MIN_OF_YOUTH_AGE && age < MIN_OF_ADULT_AGE;
    }
}
