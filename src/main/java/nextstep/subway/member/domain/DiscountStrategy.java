package nextstep.subway.member.domain;

import nextstep.subway.member.dto.Money;

public interface DiscountStrategy {

    Money discount(Money money);

}
