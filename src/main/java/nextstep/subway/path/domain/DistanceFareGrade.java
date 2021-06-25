package nextstep.subway.path.domain;
import static java.lang.Integer.*;
import static nextstep.subway.path.domain.Fare.*;

import java.util.stream.Stream;

enum DistanceFareGrade {
	BASIC(1, 10, distance -> Fare.BASIC),

	MIDDLE(11, 50, distance -> {
		int overDistance = distance - BASIC.maxDistance;
		return Fare.BASIC.plus(calculateOverFare(overDistance, 5));
	}),

	LONG(51, MAX_VALUE, distance -> {
		int overDistance = distance - MIDDLE.maxDistance;
		return MIDDLE.calculateFare(MIDDLE.maxDistance).plus(calculateOverFare(overDistance, 8));
	});

	private final int minDistance;
	private final int maxDistance;
	private final DistanceFareCalculator fareCalculator;

	DistanceFareGrade(int minDistance, int maxDistance, DistanceFareCalculator distanceFareCalculator) {
		this.minDistance = minDistance;
		this.maxDistance = maxDistance;
		this.fareCalculator = distanceFareCalculator;
	}

	static DistanceFareGrade of(int totalDistance) {
		return Stream.of(DistanceFareGrade.values())
			.filter(distanceFareGrade -> distanceFareGrade.include(totalDistance))
			.findAny()
			.orElseThrow(IllegalStateException::new);
	}

	Fare calculateFare(int distance) {
		return fareCalculator.calculateFare(distance);
	}

	private static Fare calculateOverFare(int overDistance, int extraDistanceUnit) {
		return EXTRA_FARE_UNIT.times((overDistance - 1) / extraDistanceUnit + 1);
	}

	private boolean include(int distance) {
		return this.minDistance <= distance && distance <= this.maxDistance;
	}
}
