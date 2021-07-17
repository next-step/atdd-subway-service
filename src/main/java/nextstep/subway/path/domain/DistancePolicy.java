package nextstep.subway.path.domain;

import java.util.Arrays;

public enum DistancePolicy {
	FIRST_TARGET(0, 10, 0),
	SECOND_TARGET(10, 50, 5),
	THIRD_TARGET(50, Integer.MAX_VALUE, 8);

	private static final int BASE_FARE = 1250;
	private static final int BASE_LENGTH = 10;
	private static final int ADDITIONAL_FARE = 100;

	private int minDistance;
	private int maxDistance;
	private int term;

	DistancePolicy(int minDistance, int maxDistance, int term) {
		this.minDistance = minDistance;
		this.maxDistance = maxDistance;
		this.term = term;
	}

	public static int getFareByDistance(int distance) {
		DistancePolicy distancePolicy = getDistancePolicyByDistance(distance);

		if(distancePolicy == FIRST_TARGET) {
			return BASE_FARE;
		}
		return BASE_FARE + distancePolicy.calculateOverFare(distance - BASE_LENGTH);
	}

	public static DistancePolicy getDistancePolicyByDistance(int distance) {
		return Arrays.stream(DistancePolicy.values())
			.filter(distancePolicy -> distancePolicy.minDistance < distance && distancePolicy.maxDistance >= distance)
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}

	private int calculateOverFare(int distance) {
		return (int) ((Math.ceil((distance - 1) / term) + 1) * ADDITIONAL_FARE);
	}
}
