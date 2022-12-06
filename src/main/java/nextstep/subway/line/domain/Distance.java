package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.util.Assert;

@Embeddable
public class Distance {

	@Column(name = "distance")
	private int value;

	protected Distance() {
	}

	private Distance(int value) {
		Assert.isTrue(isPositive(value), "거리는 0보다 커야 합니다.");
		this.value = value;
	}

	public static Distance from(int value) {
		return new Distance(value);
	}

	public int value() {
		return value;
	}

	private boolean isPositive(int value) {
		return value > 0;
	}

	public Distance subtract(Distance distance) {
		return new Distance(this.value - distance.value);
	}

	public Distance add(Distance distance) {
		return new Distance(this.value + distance.value);
	}

	public boolean lessOrEqual(Distance distance) {
		return this.value <= distance.value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Distance distance1 = (Distance)o;
		return value == distance1.value;
	}

	@Override
	public int hashCode() {
		return value;
	}

	@Override
	public String toString() {
		return "value = " + value;
	}
}
