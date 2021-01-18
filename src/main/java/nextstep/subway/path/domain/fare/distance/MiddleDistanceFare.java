package nextstep.subway.path.domain.fare.distance;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.fare.FareUserAge;
import nextstep.subway.path.domain.fare.Money;

public class MiddleDistanceFare extends DistanceFareDecorator {
	private static final int MINIMUM_DISTANCE = 10;
	private static final int INCREASE_DISTANCE = 5;
	
	@Override
	public Money calculate(final Distance distance, FareUserAge ageFare) {
		return super.calculate(distance, ageFare).plus(getFare(distance, ageFare));
	}

	private Money getFare(Distance distance, FareUserAge ageFare) {
		return ageFare.discount(middleDistanceFare(distance));
	}

	private Money middleDistanceFare(Distance distance) {
		return super.calculateDistance(distance.minus(MINIMUM_DISTANCE), INCREASE_DISTANCE);
	}
}
