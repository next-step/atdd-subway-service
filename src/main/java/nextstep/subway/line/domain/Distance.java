package nextstep.subway.line.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

import nextstep.subway.common.exception.Exceptions;

@Embeddable
public class Distance {
	private int distance;

	public Distance(int distance) {
		this.distance = distance;
	}

	protected Distance() {
	}

	public boolean isLessOrEqualThan(int distance) {
		return this.distance <= distance;
	}

	public int get() {
		return this.distance;
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

	public Distance minus(int newDistance) {
		if (isLessOrEqualThan(newDistance)) {
			throw Exceptions.DISTANCE_TOO_FAR.getException();
		}

		return new Distance(this.distance - newDistance);
	}
}
