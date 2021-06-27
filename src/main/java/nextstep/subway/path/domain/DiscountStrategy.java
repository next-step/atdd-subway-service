package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;

interface DiscountStrategy {
	Fare discountFare(Fare originFare);
}
