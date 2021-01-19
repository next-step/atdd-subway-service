package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

public enum DistanceFare {

	BASIC(0, 10, (distance) -> 1250)
	, EXTRA_MEDIUM (10, 50, (distance) -> {
		int calculatedDistance = findCalculateDistance(distance, 10, 50);
		return calculateDistanceFare(calculatedDistance, 5, 100);
	})
	, EXTRA_LONG (50, null, (distance) -> {
		int calculatedDistance = distance - 50;
		return calculateDistanceFare(calculatedDistance, 8, 100);
	});


	private int minDistance;
	private Optional<Integer> maxDistance;
	private Function<Integer, Integer> calculateFare;


	DistanceFare(int minDistance, Integer maxDistance, Function<Integer, Integer> calculateFare) {
		this.minDistance = minDistance;
		this.maxDistance = Optional.ofNullable(maxDistance);
		this.calculateFare = calculateFare;
	}

	public int calculateFare(int distance) {
		return Arrays.stream(DistanceFare.values())
			.filter(fare -> fare.compareTo(this) <= 0)
			.mapToInt(fare -> fare.calculateFare.apply(distance))
			.sum();
	}

	public static DistanceFare valueOfDistance(int distance) {
		return Arrays.stream(DistanceFare.values())
			.filter(fare -> fare.isBetweenMinAndMax(distance))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("요금 정책에 맞지 않은 경로 입니다."));
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

	private static int findCalculateDistance(int distance, int minDistance, int maxDistance) {
		return Math.min(distance, maxDistance) - minDistance;
	}

	private static int calculateDistanceFare(int distance, int chargingDistance, int overFare) {
		return (int) ((Math.ceil((distance - 1) / chargingDistance) + 1) * overFare);
	}
}
