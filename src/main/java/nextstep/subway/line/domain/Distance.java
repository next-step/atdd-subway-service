package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {
	private static final int MINIMUM_DISTANCE = 1;
	private static final int BASELINE_LONG_DISTANCE = 50;
	private static final int BASELINE_MIDDLE_DISTANCE = 10;
	private int distance;

	protected Distance() {
	}

	public Distance(int distance) {
		validateDistanceValue(distance);
		this.distance = distance;
	}

	private void validateDistanceValue(int distance) {
		if (distance < MINIMUM_DISTANCE) {
			throw new IllegalArgumentException("구간은 1 이상의 거리를 가질 수 있습니다.");
		}
	}

	public int getDistance() {
		return distance;
	}

	public int calculatePlusDistance(Distance distance) {
		return this.distance + distance.distance;
	}

	public void minusDistance(Distance distance) {
		this.distance -= distance.distance;
	}

	public boolean isLessThanOrEqualTo(Distance distance) {
		return this.distance <= distance.distance;
	}

	public Double toWeight() {
		return Double.valueOf(this.distance);
	}

	public boolean isLongDistance() {
		return this.distance > BASELINE_LONG_DISTANCE;
	}

	public int getLongDistance() {
		if (isLongDistance()) {
			return this.distance - BASELINE_LONG_DISTANCE;
		}
		return 0;
	}

	public int getMiddleDistance() {
		if (isLongDistance()) {
			return BASELINE_LONG_DISTANCE - BASELINE_MIDDLE_DISTANCE;
		}
		if (hasMiddleDistance()) {
			return distance - BASELINE_MIDDLE_DISTANCE;
		}
		return 0;
	}

	public boolean hasMiddleDistance() {
		return this.distance > BASELINE_MIDDLE_DISTANCE;
	}
}
