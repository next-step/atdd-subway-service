package nextstep.subway.line.infrastructure.fare.policycondition;

import java.math.BigDecimal;
import nextstep.subway.line.domain.Money;
import nextstep.subway.line.domain.policycondition.AgeDiscountFareCondition;
import nextstep.subway.member.domain.Age;
import nextstep.subway.member.domain.AgeGroup;

public class ChildDiscountCondition implements AgeDiscountFareCondition {

    private static final AgeGroup AGE_TYPE = AgeGroup.CHILD;
    private static final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.5);

    public static ChildDiscountCondition of() {
        return new ChildDiscountCondition();
    }

    @Override
    public Money discountFare(Money money, Age age) {
        if (!AGE_TYPE.isMatch(age)) {
            return money;
        }

        return calculate(money, DISCOUNT_RATE);
    }
}
