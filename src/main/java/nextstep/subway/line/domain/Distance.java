package nextstep.subway.line.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {
	public static String DISTANCE_GREATER_THAN_ZERO = "거리는 0보다 커야 합니다.";
	private int distance;

	protected Distance() {
	}

	public Distance(int distance) {
		validate(distance);
		this.distance = distance;
	}

	public Distance plus(Distance distance) {
		return new Distance(this.distance + distance.distance);
	}

	public Distance minus(Distance distance) {
		return new Distance((this.distance - distance.distance));
	}

	private void validate(int distance) {
		if (distance <= 0) {
			throw new IllegalArgumentException(DISTANCE_GREATER_THAN_ZERO);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Distance distance1 = (Distance)o;
		return distance == distance1.distance;
	}

	@Override
	public int hashCode() {
		return Objects.hash(distance);
	}
}
