package nextstep.subway.line.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {
	public static final Distance DUMMY_DISTANCE = new Distance(0);
	private final int distance;

	protected Distance() {
		this.distance = 0;
	}

	private Distance(final int distance) {
		this.distance = distance;
	}

	public static Distance of(int distance) {
		return new Distance(distance);
	}

	public static Distance of(double distance) {
		return new Distance((int)distance);
	}

	public boolean lessThan(Distance distance) {
		return this.distance < distance.distance;
	}

	public Distance decrease(Distance distance) {
		return new Distance(this.distance - distance.distance);
	}

	public Distance increase(Distance distance) {
		return new Distance(this.distance + distance.distance);
	}

	public int value() {
		return distance;
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
