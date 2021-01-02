package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DistanceFarePolicy {

	OVER_TEN_TO_FIFTY(5, 100, 10, 50),
	OVER_FIFTY(8, 100, 50, Integer.MAX_VALUE);

	public static final int BASIC_DISTANCE = 10;
	public static final int EXTRA_DISTANCE = 50;
	private final int unitDistance;
	private final int unitFare;
	private final Integer min;
	private final Integer max;

	public static List<DistanceFarePolicy> findDistanceGroup(int distance) {
		return Arrays.stream(DistanceFarePolicy.values())
			.filter(distanceFarePolicy -> distanceFarePolicy.getMin() < distance)
			.collect(Collectors.toList());
	}

	public int calculateOverDistance(int distance) {
		if (distance <= this.min) {
			return 0;
		}
		int overDistance = distance - this.min;
		if (distance > this.max) {
			overDistance = this.max - this.min;
		}
		return overDistance;
	}
}
