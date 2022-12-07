package nextstep.subway.path.fare.discount;

import nextstep.subway.path.fare.Fare;

public interface AgeDiscountStrategy {
    Fare discount(Fare fare);
}
