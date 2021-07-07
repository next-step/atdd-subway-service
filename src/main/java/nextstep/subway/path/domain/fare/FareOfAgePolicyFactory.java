package nextstep.subway.path.domain.fare;

import java.util.Arrays;

public enum FareOfAgePolicyFactory {
    NONE(new DefaultAgePolicy()),
    TEENAGER(new AgeOfTeenagerPolicy()),
    CHILDREN(new AgeOfChildrenPolicy());

    private DiscountOfAgeCalculator discountOfAgeCalculator;

    FareOfAgePolicyFactory(final DiscountOfAgeCalculator discountOfAgeCalculator) {
        this.discountOfAgeCalculator = discountOfAgeCalculator;
    }

    public static int discount(final int age, final int totalFare) {
        FareOfAgePolicyFactory fareOfAgePolicyFactory = Arrays.stream(values())
                                                .filter(policy -> policy.isTarget(age))
                                                .findFirst()
                                                .orElse(NONE);
        return fareOfAgePolicyFactory.acceptDiscount(totalFare);
    }

    private boolean isTarget(final int age) {
        return this.discountOfAgeCalculator.isTarget(age);
    }

    private int acceptDiscount(final int totalFare) {
        return this.discountOfAgeCalculator.discount(totalFare);
    }
}
