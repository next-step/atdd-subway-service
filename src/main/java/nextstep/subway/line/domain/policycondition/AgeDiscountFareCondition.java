package nextstep.subway.line.domain.policycondition;

import java.math.BigDecimal;
import nextstep.subway.line.domain.Money;
import nextstep.subway.member.domain.Age;

public interface AgeDiscountFareCondition {

    default Money calculate(Money money, BigDecimal discountRate) {
        Money discount = money.minus(DEDUCTIBLE_AMOUNT).multiply(discountRate);
        return money.minus(DEDUCTIBLE_AMOUNT + discount.toInt());
    }

    int DEDUCTIBLE_AMOUNT = 350;

    Money discountFare(Money money, Age age);
}
