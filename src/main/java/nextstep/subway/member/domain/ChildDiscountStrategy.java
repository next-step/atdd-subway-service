package nextstep.subway.member.domain;

import nextstep.subway.member.dto.Money;

public class ChildDiscountStrategy implements DiscountStrategy {

    @Override
    public Money discount(Money money) {
        Money discount = money.subtract(Money.of(350)).multiply(0.5);
        return money.subtract(discount);
    }

}
