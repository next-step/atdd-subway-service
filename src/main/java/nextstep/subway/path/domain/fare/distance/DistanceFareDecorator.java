package nextstep.subway.path.domain.fare.distance;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.fare.FareUserAge;
import nextstep.subway.path.domain.fare.Money;

public abstract class DistanceFareDecorator implements DistanceFare {

	private static final int INCREASE_DISTANCE_FARE = 100;

	@Override
	public Money calculate(final Distance distance, FareUserAge ageFare) {
		return new DefaultDistanceFare().calculate(distance, ageFare);
	}

	public Money calculateDistance(int distance, int rangeDistance) {
		int range = (distance - 1) / rangeDistance;
		return Money.of((int)((Math.ceil(range) + 1) * INCREASE_DISTANCE_FARE));
	}
}
