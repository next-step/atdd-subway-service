package nextstep.subway.line.domain;

import java.util.Arrays;

public enum DistanceFarePolicy {

	DEFAULT(0, 10, 1, 0),
	STEP_1(10, 50, 5, 100),
	STEP_2(50, Integer.MAX_VALUE, 8, 100)
	;

	private static int DEFAULT_DISTANCE_FARE = 1250;

	private final int greaterThanDistance;
	private final int lessOrEqualsDistance;
	private final int stepDistance;
	private final int stepFare;

	DistanceFarePolicy(final int greaterThanDistance, final int lessOrEqualsDistance, final int stepDistance,
		final int stepFare) {
		this.greaterThanDistance = greaterThanDistance;
		this.lessOrEqualsDistance = lessOrEqualsDistance;
		this.stepDistance = stepDistance;
		this.stepFare = stepFare;
	}

	public static Fare calculateDistanceFare(int distance) {
		if (distance <= DEFAULT.lessOrEqualsDistance) {
			return new Fare(DEFAULT_DISTANCE_FARE);
		}

		int fee = DEFAULT_DISTANCE_FARE;
		int step;

		while (distance > 0) {
			final int finalDistance = distance;
			DistanceFarePolicy distanceFarePolicy = DistanceFarePolicy.getInstance(finalDistance);
			fee += distanceFarePolicy.stepFare;
			step = distanceFarePolicy.stepDistance;

			distance -= step;
		}

		return new Fare(fee);
	}

	private static DistanceFarePolicy getInstance(final int finalDistance) {
		return Arrays.stream(DistanceFarePolicy.values())
			.filter(policy -> policy.isIn(finalDistance))
			.findFirst()
			.orElseThrow(RuntimeException::new);
	}

	private boolean isIn(final int distance) {
		return this.greaterThanDistance < distance && distance <= this.lessOrEqualsDistance;
	}
}
