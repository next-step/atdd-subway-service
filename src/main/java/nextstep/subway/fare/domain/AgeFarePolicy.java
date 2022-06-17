package nextstep.subway.fare.domain;

import java.util.Arrays;
import nextstep.subway.member.domain.AgeGroup;

public enum AgeFarePolicy {
    TEENAGER_POLICY(AgeGroup.TEEN, 80),
    CHILDREN_POLICY(AgeGroup.CHILD, 50),
    NONE_POLICY(AgeGroup.ETC, 0);

    private static final int DEDUCTIBLE_FARE = 350;
    public static final int ONE_HUNDRED = 100;
    private final AgeGroup ageGroup;
    private final int discountedFare;

    AgeFarePolicy(AgeGroup ageGroup, int discountedFare) {
        this.ageGroup = ageGroup;
        this.discountedFare = discountedFare;
    }

    public static AgeFarePolicy findAgeFarePolicyByAgeGroup(AgeGroup ageGroup) {
        return Arrays.stream(AgeFarePolicy.values())
                .filter(ageFarePolicy -> ageFarePolicy.ageGroup.equals(ageGroup))
                .findFirst()
                .orElse(AgeFarePolicy.NONE_POLICY);
    }

    public void discountFare(Fare fare) {
        if (isEtcAgeGroup()) {
            return;
        }
        fare.minus(Fare.valueOf(DEDUCTIBLE_FARE));
        fare.multiply(discountedFare);
        fare.divideBy(ONE_HUNDRED);
    }

    private boolean isEtcAgeGroup() {
        return ageGroup.equals(AgeGroup.ETC);
    }
}
