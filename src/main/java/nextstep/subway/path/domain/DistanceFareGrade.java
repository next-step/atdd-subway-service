package nextstep.subway.path.domain;
import static java.lang.Integer.*;

import java.util.stream.Stream;

import nextstep.subway.line.domain.Fare;

enum DistanceFareGrade {
	BASIC(1, 10, totalDistance -> Fare.wonOf(1250)),
	MID(11, 50, totalDistance -> {
		int overDistance = totalDistance - BASIC.endInclusive;
		return BASIC.calculateDistanceFare(BASIC.endInclusive).plus(calculateDistanceFare(overDistance, 5));
	}),
	LONG(51, MAX_VALUE, totalDistance -> {
		int overDistance = totalDistance - MID.endInclusive;
		return MID.calculateDistanceFare(MID.endInclusive).plus(calculateDistanceFare(overDistance, 8));
	});

	static final Fare EXTRA_FARE_UNIT = Fare.wonOf(100);

	private final int startInclusive;
	private final int endInclusive;
	private final DistanceFareStrategy fareCalculator;

	DistanceFareGrade(int startInclusive, int endInclusive, DistanceFareStrategy distanceFareStrategy) {
		this.startInclusive = startInclusive;
		this.endInclusive = endInclusive;
		this.fareCalculator = distanceFareStrategy;
	}

	static DistanceFareGrade of(int distance) {
		return Stream.of(DistanceFareGrade.values())
			.filter(distanceFareGrade -> distanceFareGrade.matchDistance(distance))
			.findAny()
			.orElseThrow(IllegalStateException::new);
	}

	Fare calculateDistanceFare(int distance) {
		return fareCalculator.calculateDistanceFare(distance);
	}

	private static Fare calculateDistanceFare(int overDistance, int extraDistanceUnit) {
		return EXTRA_FARE_UNIT.times((overDistance - 1) / extraDistanceUnit + 1);
	}

	private boolean matchDistance(int distance) {
		return this.startInclusive <= distance && distance <= this.endInclusive;
	}
}
