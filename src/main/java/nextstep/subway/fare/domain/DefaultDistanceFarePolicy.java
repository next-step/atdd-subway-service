package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Distance;

public class DefaultDistanceFarePolicy implements DistanceFarePolicy {

	private static final int MIN_DISTANCE = 1;
	private static final int MAX_DISTANCE = 10;

	@Override
	public Fare calculate(Distance distance) {
		return new Fare(DEFAULT_FARE);
	}

	@Override
	public boolean isAccepted(Distance distance) {
		return distance.value() >= MIN_DISTANCE && distance.value() <= MAX_DISTANCE;
	}
}
