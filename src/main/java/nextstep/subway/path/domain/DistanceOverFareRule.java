package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

public enum DistanceOverFareRule {

	BASIC(0, 10, (distance) -> 1250)
	, MEDIUM (10, 50, (distance) -> {
		int calculatedDistance = findCalculateDistance(distance, 10, 50);
		return calculateDistanceFare(calculatedDistance, 5, 100);
	})
	, LONG (50, null, (distance) -> {
		int calculatedDistance = distance - 50;
		return calculateDistanceFare(calculatedDistance, 8, 100);
	});


	private int minDistance;
	private Optional<Integer> maxDistance;
	private Function<Integer, Integer> calculateFare;


	DistanceOverFareRule(int minDistance, Integer maxDistance, Function<Integer, Integer> calculateFare) {
		this.minDistance = minDistance;
		this.maxDistance = Optional.ofNullable(maxDistance);
		this.calculateFare = calculateFare;
	}

	public int calculateFare(int distance) {
		return Arrays.stream(DistanceOverFareRule.values())
			.filter(fare -> fare.compareTo(this) <= 0)
			.mapToInt(fare -> fare.calculateFare.apply(distance))
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

	private int findMaxDistance(int distance) {
		int maxDistance = distance;
		if (this.maxDistance.isPresent()) {
			maxDistance = this.maxDistance.get();
		}
		return maxDistance;
	}

	private static int findCalculateDistance(int distance, int minDistance, int maxDistance) {
		return Math.min(distance, maxDistance) - minDistance;
	}

	private static int calculateDistanceFare(int distance, int chargingDistance, int overFare) {
		return (int) ((Math.ceil((distance - 1) / chargingDistance) + 1) * overFare);
	}
}
