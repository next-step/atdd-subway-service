package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Distance;

public class NearDistanceFarePolicy implements DistanceFarePolicy {

	private static final int MIN_DISTANCE = 11;
	private static final int MAX_DISTANCE = 50;
	private static final int DISTANCE_ONE_KM_FARE = 100;
	private static final int DISTANCE_RATE = 5;

	@Override
	public Fare calculate(Distance distance) {
		return new Fare(DEFAULT_FARE + (int)((Math.ceil((distance.value() - 1) / DISTANCE_RATE) + 1) * DISTANCE_ONE_KM_FARE));
	}

	@Override
	public boolean isAccepted(Distance distance) {
		return distance.value() >= MIN_DISTANCE && distance.value() <= MAX_DISTANCE;
	}
}
