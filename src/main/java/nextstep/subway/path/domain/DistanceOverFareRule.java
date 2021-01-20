package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.Optional;

public enum DistanceOverFareRule {

	BASIC(0, 10, 0, 0)
	, MEDIUM_SECTION (10, 50, 5, 100)
	, LONG_SECTION (50, null, 8, 100);


	private int minDistance;
	private Optional<Integer> maxDistance;
	private int chargingDistance;
	private int overFare;

	private static final int MIN_FARE = 1250;

	DistanceOverFareRule(int minDistance, Integer maxDistance, int chargingDistance, int overFare) {
		this.minDistance = minDistance;
		this.maxDistance = Optional.ofNullable(maxDistance);;
		this.chargingDistance = chargingDistance;
		this.overFare = overFare;
	}

	public int calculateFare(int distance) {
		return MIN_FARE + Arrays.stream(DistanceOverFareRule.values())
			.filter(fare -> fare.compareTo(this) <= 0 && fare.chargingDistance != 0)
			.mapToInt(fare -> fare.calculateDistanceFare(distance))
			.sum();
	}

	public static DistanceOverFareRule valueOfDistance(int distance) {
		return Arrays.stream(DistanceOverFareRule.values())
			.filter(fare -> fare.isBetweenMinAndMax(distance))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("요금 정책에 맞지 않은 경로 입니다."));
	}

	private boolean isBetweenMinAndMax(int distance) {
		int maxDistance = findMaxDistance(distance);
		return distance > this.minDistance && distance <= maxDistance;
	}

	private int calculateDistanceFare(int distance) {
		int distanceInSection = findDistanceInSection(distance);
		return (int) ((Math.ceil((distanceInSection - 1) / chargingDistance) + 1) * overFare);
	}

	private int findDistanceInSection(int distance) {
		return Math.min(findMaxDistance(distance), distance) - minDistance;
	}

	private int findMaxDistance(int distance) {
		int maxDistance = distance;
		if (this.maxDistance.isPresent()) {
			maxDistance = this.maxDistance.get();
		}
		return maxDistance;
	}
}
