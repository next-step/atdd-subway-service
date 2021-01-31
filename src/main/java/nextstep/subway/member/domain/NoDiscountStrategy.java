package nextstep.subway.member.domain;

import nextstep.subway.member.dto.Money;

public class NoDiscountStrategy implements DiscountStrategy {

    @Override
    public Money discount(Money money) {
        return money;
    }
}
