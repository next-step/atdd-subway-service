package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;

public interface DiscountStrategy {
	Fare discountFare(Fare originFare);
}
