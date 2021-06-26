package nextstep.subway.path.domain;
import static java.lang.Integer.*;

import java.util.stream.Stream;

import nextstep.subway.line.domain.Fare;

enum OverDistanceFareGrade {
	BASIC(1, 10, distance -> Fare.ZERO),

	MIDDLE(11, 50, distance -> {
		int overDistance = distance - BASIC.maxDistance;
		return calculateOverFare(overDistance, 5);
	}),

	LONG(51, MAX_VALUE, distance -> {
		int overDistance = distance - MIDDLE.maxDistance;
		return MIDDLE.calculateOverFare(MIDDLE.maxDistance).plus(calculateOverFare(overDistance, 8));
	});

	static final Fare EXTRA_FARE_UNIT = Fare.wonOf(100);

	private final int minDistance;
	private final int maxDistance;
	private final OverDistanceFareCalculator fareCalculator;

	OverDistanceFareGrade(int minDistance, int maxDistance, OverDistanceFareCalculator overDistanceFareCalculator) {
		this.minDistance = minDistance;
		this.maxDistance = maxDistance;
		this.fareCalculator = overDistanceFareCalculator;
	}

	static OverDistanceFareGrade of(int totalDistance) {
		return Stream.of(OverDistanceFareGrade.values())
			.filter(overDistanceFareGrade -> overDistanceFareGrade.include(totalDistance))
			.findAny()
			.orElseThrow(IllegalStateException::new);
	}

	Fare calculateOverFare(int distance) {
		return fareCalculator.calculateOverDistanceFare(distance);
	}

	private static Fare calculateOverFare(int overDistance, int extraDistanceUnit) {
		return EXTRA_FARE_UNIT.times((overDistance - 1) / extraDistanceUnit + 1);
	}

	private boolean include(int distance) {
		return this.minDistance <= distance && distance <= this.maxDistance;
	}
}
