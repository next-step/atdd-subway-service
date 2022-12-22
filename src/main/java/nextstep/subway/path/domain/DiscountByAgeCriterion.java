package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.path.constants.PathErrorMessages;

public enum DiscountByAgeCriterion {

    CHILD(6, 13, 0.50),
    TEENAGER(13, 19, 0.20),
    ADULT(20, null, 0.00);

    private Integer minAge; // 대상 최소 나이 (minAge 일치 시 대상 포함)
    private Integer maxAge; // 대상 최대 나이 (maxAge 일치 시 대상 미포함)
    private double discountFactor; // 할인율

    DiscountByAgeCriterion(Integer minAge, Integer maxAge, double discountFactor) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discountFactor = discountFactor;
    }

    public static DiscountByAgeCriterion checkDiscountByAgeCriterion(Integer age) {
        List<DiscountByAgeCriterion> list = Arrays.stream(DiscountByAgeCriterion.values()).collect(Collectors.toList());
        return list.stream()
                .sorted(Comparator.reverseOrder())
                .filter(discountByAgeCriterion
                        -> age >= discountByAgeCriterion.minAge
                        && (discountByAgeCriterion.maxAge == null || age < discountByAgeCriterion.maxAge))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException(PathErrorMessages.CANNOT_CHECK_DISCOUNT_CRITERION_BY_AGE));
    }

    public double getDiscountFactor() {
        return discountFactor;
    }
}
