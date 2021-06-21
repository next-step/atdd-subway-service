package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {

	private static final int MIN_DISTANCE = 0;

	private int distance;

	protected Distance() {

	}

	public Distance(int distance) {
		validate(distance);
		this.distance = distance;
	}

	private void validate(int distance) {
		if (distance <= MIN_DISTANCE)
			throw new RuntimeException("거리는 0보다 같거나 작을 수 없습니다.");
	}

	public Distance plus(Distance distance) {
		return new Distance(this.distance + distance.distance);
	}

	public Distance minus(Distance distance) {
		return new Distance(this.distance - distance.distance);
	}

	public boolean isLessThanOrEqual(Distance newDistance) {
		return this.distance <= newDistance.distance;
	}
}