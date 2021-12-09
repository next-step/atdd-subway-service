package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {

	public static final int MIN_INCLUSIVE = 1;

	private int distance;

	public Distance() {
	}

	public Distance(int distance) {
		validate(distance);
		this.distance = distance;
	}

	private void validate(int distance) {
		if (distance < MIN_INCLUSIVE) {
			throw new IllegalArgumentException(String.format("거리는 %d 이상이어야 합니다.", MIN_INCLUSIVE));
		}
	}

	public Distance plus(int distance) {
		return new Distance(this.distance + distance);
	}

	public Distance minus(int distance) {
		return new Distance(this.distance - distance);
	}

	public boolean isLessThanOrEqualTo(int distance) {
		return this.distance <= distance;
	}

	public int getDistance() {
		return distance;
	}
}
