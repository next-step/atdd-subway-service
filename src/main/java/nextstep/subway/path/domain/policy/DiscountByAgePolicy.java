package nextstep.subway.path.domain.policy;

public class DiscountByAgePolicy implements DiscountPolicy {
    private static final int MIN_OF_CHILDREN_AGE = 6;
    private static final int MIN_OF_YOUTH_AGE = 13;
    private static final int MIN_OF_ADULT_AGE = 19;
    private static final int MIN_OF_OLDMAN_AGE = 65;

    @Override
    public int discount(int sourceFare, int age) {
        if (isPreschoolerOrOldMan(age)) {
            return 0;
        }

        if (isChild(age)) {
            return (int) ((sourceFare - 350) * 0.5);
        }

        if (isYouth(age)) {
            return (int) ((sourceFare - 350) * 0.8);
        }

        return sourceFare;
    }

    private boolean isPreschoolerOrOldMan(int age) {
        return age < MIN_OF_CHILDREN_AGE || age >= MIN_OF_OLDMAN_AGE;
    }

    private boolean isChild(int age) {
        return age >= MIN_OF_CHILDREN_AGE && age < MIN_OF_YOUTH_AGE;
    }

    private boolean isYouth(int age) {
        return age >= MIN_OF_YOUTH_AGE && age < MIN_OF_ADULT_AGE;
    }
}
