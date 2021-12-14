package nextstep.subway.line.infrastructure.fare.policycondition;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import nextstep.subway.line.domain.Money;
import nextstep.subway.line.domain.fare.policycondition.AgeDiscountFareCondition;
import nextstep.subway.member.domain.Age;
import nextstep.subway.member.domain.AgeGroup;

public enum AgeRatePolicyCondition implements AgeDiscountFareCondition {
    TWENTY_RATE(BigDecimal.valueOf(0.2), Arrays.asList(AgeGroup.YOUTH)),
    FIFTY_RATE(BigDecimal.valueOf(0.5), Arrays.asList(AgeGroup.CHILD)),
    NONE(BigDecimal.valueOf(1), Collections.emptyList());

    private final BigDecimal discountRate;
    private final List<AgeGroup> ageGroups;

    AgeRatePolicyCondition(BigDecimal discountRate, List<AgeGroup> ageGroups) {
        this.discountRate = discountRate;
        this.ageGroups = ageGroups;
    }

    @Override
    public Money discountFare(Money money, Age age) {
        AgeGroup target = AgeGroup.valueOf(age);
        if (ageGroups.contains(target)) {
            return calculate(money, discountRate);
        }

        return money;
    }
}
