package nextstep.subway.line.infrastructure.fare.policycondition;

import java.math.BigDecimal;
import nextstep.subway.line.domain.Money;
import nextstep.subway.line.domain.fare.policycondition.AgeDiscountFareCondition;
import nextstep.subway.member.domain.Age;
import nextstep.subway.member.domain.AgeGroup;

public class YouthDiscountCondition implements AgeDiscountFareCondition {

    private static final AgeGroup AGE_TYPE = AgeGroup.YOUTH;
    private static final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.2);

    public static YouthDiscountCondition of() {
        return new YouthDiscountCondition();
    }

    @Override
    public Money discountFare(Money money, Age age) {
        if (!AGE_TYPE.isMatch(age.getAge())) {
            return money;
        }

        return calculate(money, DISCOUNT_RATE);
    }
}
