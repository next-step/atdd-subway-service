package nextstep.subway.path.domain;

import java.util.function.Function;

import nextstep.subway.common.domain.Fare;
import nextstep.subway.line.domain.Distance;

public enum DistanceFarePolicy {

	DEFAULT(10, distance -> DistanceFareFactory.defaultFare()),
	MEDIUM(50, DistanceFareFactory::mediumDistanceFare),
	LONG(Integer.MAX_VALUE, DistanceFareFactory::longDistanceFare);

	private static final int ZERO = 0;
	private final int maxDistance;
	private final Function<Distance, Fare> fareCalculator;

	DistanceFarePolicy(int maxDistance, Function<Distance, Fare> fareCalculator) {
		this.maxDistance = maxDistance;
		this.fareCalculator = fareCalculator;
	}

	public static Fare calculate(Distance distance) {
		DistanceFarePolicy policy = of(distance);
		return policy.fareCalculator.apply(distance);
	}

	private static DistanceFarePolicy of(Distance distance) {
		if (distance.isInClosedOpenRange(ZERO, DEFAULT.maxDistance)) {
			return DEFAULT;
		}

		if (distance.isInClosedOpenRange(DEFAULT.maxDistance, MEDIUM.maxDistance)) {
			return MEDIUM;
		}

		return LONG;
	}

	Distance maxDistance() {
		return Distance.from(this.maxDistance);
	}
}
