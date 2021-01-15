package nextstep.subway.fare.domain;

import java.util.Arrays;

import nextstep.subway.line.domain.Distance;

public enum DistanceCalculator {

	SHORT_DISTANCE(10, 0),
	MIDDLE_DISTANCE(50, 5),
	LONG_DISTANCE(Integer.MAX_VALUE, 8);

	private static final Money BASIC_FARE = Money.of(1250L);
	private static final int INCREASE_FARE_AMOUNT = 100;

	private final int maxDistance;
	private final int increaseFareByDistance;

	DistanceCalculator(final int maxDistance, final int increaseFareByDistance) {
		this.maxDistance = maxDistance;
		this.increaseFareByDistance = increaseFareByDistance;
	}

	public static Money apply(Distance distance) {
		return calculator(distance);
	}

	public static Money calculator(Distance distance) {
		DistanceCalculator distanceCalculator = Arrays.stream(values())
			.filter(it -> it.maxDistance >= distance.getDistance())
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);

		return distanceCalculator.calculateOverFare(distance);
	}

	private Money calculateOverFare(Distance distance) {
		if (this.increaseFareByDistance == 0) {
			return BASIC_FARE;
		}
		int range = (distance.getDistance() - 1) / this.increaseFareByDistance;
		return BASIC_FARE.plus((int)((Math.ceil(range) + 1) * INCREASE_FARE_AMOUNT));
	}
}
