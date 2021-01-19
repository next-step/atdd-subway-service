package nextstep.subway.path.domain.fare.distance;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.fare.FareUserAge;
import nextstep.subway.path.domain.fare.Money;

public class LongDistanceFare extends MiddleDistanceFare {
	private static final int MINIMUM_DISTANCE = 50;
	private static final int INCREASE_DISTANCE = 8;

	@Override
	public Money calculate(Distance distance, FareUserAge ageFare) {
		return super.calculate(Distance.of(MINIMUM_DISTANCE), ageFare).plus(getFare(distance, ageFare));
	}

	private Money getFare(Distance distance, FareUserAge ageFare) {
		return ageFare.discount(longDistanceFare(distance));
	}

	private Money longDistanceFare(Distance distance) {
		return super.calculateDistance(distance.minus(MINIMUM_DISTANCE), INCREASE_DISTANCE);
	}
}
