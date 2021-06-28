package nextstep.subway.path.domain;

import java.util.Arrays;

public enum DistanceFare {
	DEFAULT(1, 10, distance -> 1250),
	FIRST_EXTRA(11, 50, distance -> {
		int extraDistance = distance - DEFAULT.end;
		return DEFAULT.calculateFare(DEFAULT.end) + calculateOverFare(extraDistance, 5);
	}),
	SECOND_EXTRA(51, 10000, distance -> {
		int extraDistance = distance - FIRST_EXTRA.end;
		return FIRST_EXTRA.calculateFare(FIRST_EXTRA.end) + calculateOverFare(extraDistance, 8);
	});

	private int start;
	private int end;
	private FareStrategy fareStrategy;

	DistanceFare(int start, int end, FareStrategy fareStrategy) {
		this.start = start;
		this.end = end;
		this.fareStrategy = fareStrategy;
	}

	public int calculateFare(int distance) {
		return fareStrategy.calculateFare(distance);
	}

	public static DistanceFare findByDistance(int distance) {
		return Arrays.stream(DistanceFare.values())
			.filter(distanceFare -> distanceFare.matches(distance))
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException("요금을 적용할 수 없는 거리입니다."));
	}

	private static int calculateOverFare(int overDistance, int unitDistance) {
		return (int)((Math.ceil((overDistance - 1) / unitDistance) + 1) * 100);
	}

	private boolean matches(int distance) {
		return this.start <= distance && this.end > distance;
	}
}
