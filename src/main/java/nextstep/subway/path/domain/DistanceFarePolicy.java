package nextstep.subway.path.domain;

import java.util.Arrays;

public enum DistanceFarePolicy {
	DEFAULT_FARE(0, 10, 0, 1250),
	OVER_FARE_10_50(10, 50, 5, 100),
	OVER_FARE_GREATER_THAN_50(50, Integer.MAX_VALUE, 8, 100);

	private final int minDistance;
	private final int maxDistance;
	private final double standardDistance;
	private final int overFare;

	DistanceFarePolicy(int minDistance, int maxDistance, double standardDistance, int overFare) {
		this.minDistance = minDistance;
		this.maxDistance = maxDistance;
		this.standardDistance = standardDistance;
		this.overFare = overFare;
	}

	public static int calculateFare(int distance) {
		int fare = 0;

		while (distance > 0) {
			DistanceFarePolicy farePolicy = find(distance);
			fare += farePolicy.calculateOverFare(distance - farePolicy.minDistance);
			distance = farePolicy.minDistance;
		}

		return fare;
	}

	private static DistanceFarePolicy find(int distance) {
		return Arrays.stream(DistanceFarePolicy.values())
			.filter(distanceFarePolicy -> distanceFarePolicy.isTargetDistance(distance))
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}

	private boolean isTargetDistance(int distance) {
		return distance > minDistance && distance <= maxDistance;
	}

	private int calculateOverFare(int distance) {
		return calculateOverAmount(distance) * overFare;
	}

	private int calculateOverAmount(int distance) {
		if (standardDistance == 0) {
			return 1;
		}

		return (int)Math.ceil(distance / standardDistance);
	}
}
