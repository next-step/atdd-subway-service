package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.Optional;

public enum DistanceFarePractice {

	BASIC(0, 10, 0, 0)
	, EXTRA_MEDIUM (10, 50, 5, 100)
	, EXTRA_LONG (50, null, 8, 100);

	private int minDistance;
	private Optional<Integer> maxDistance;
	private int chargingDistance;
	private int overFare;

	private static final Integer MIN_FARE = 1250;

	DistanceFarePractice(int minDistance, Integer maxDistance, int chargingDistance, int overFare) {
		this.minDistance = minDistance;
		this.maxDistance = Optional.ofNullable(maxDistance);
		this.chargingDistance = chargingDistance;
		this.overFare = overFare;
	}

	public int calculateFare(int distance) {
		return MIN_FARE + Arrays.stream(DistanceFarePractice.values())
			.filter(fare -> fare.compareTo(this) <= 0)
			.mapToInt(fare -> fare.calculateFareInSection(distance))
			.sum();
	}

	public static DistanceFarePractice valueOfDistance(int distance) {
		return Arrays.stream(DistanceFarePractice.values())
			.filter(fare -> fare.isBetweenMinAndMax(distance))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("요금 정책에 맞지 않은 경로 입니다."));
	}

	private int calculateFareInSection(int distance) {
		if (this.chargingDistance == 0) {
			return 0;
		}
		int calculatedDistance = Math.min(findMaxDistance(distance), distance) - minDistance;
		return (int) ((Math.ceil((calculatedDistance - 1) / chargingDistance) + 1) * overFare);
	}

	private boolean isBetweenMinAndMax(int distance) {
		int max = findMaxDistance(distance);
		return distance > minDistance && distance <= max;
	}

	private int findMaxDistance(int distance) {
		int max = distance;
		if (maxDistance.isPresent()) {
			max = maxDistance.get();
		}
		return max;
	}
}
