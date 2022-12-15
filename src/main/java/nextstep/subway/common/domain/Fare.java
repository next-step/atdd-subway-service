package nextstep.subway.common.domain;

import javax.persistence.Embeddable;

import io.jsonwebtoken.lang.Assert;

@Embeddable
public class Fare implements Comparable<Fare> {

	private static final Fare ZERO = new Fare(0);

	private int value;

	protected Fare() {
	}

	private Fare(int value) {
		Assert.isTrue(graterOrEqualToZero(value), "요금은 음수일 수 없습니다.");
		this.value = value;
	}

	private boolean graterOrEqualToZero(int value) {
		return value >= 0;
	}

	public static Fare from(int value) {
		return new Fare(value);
	}

	public static Fare zero() {
		return ZERO;
	}

	public Fare sum(Fare fare) {
		return from(this.value + fare.value);
	}

	public Fare multiply(Fare target) {
		return from(this.value * target.value);
	}

	public Fare subtract(Fare fare) {
		return from(this.value - fare.value);
	}

	public int value() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Fare fare = (Fare)o;

		return value == fare.value;
	}

	@Override
	public int hashCode() {
		return value;
	}

	@Override
	public String toString() {
		return "Fare{" + "value=" + value + '}';
	}

	@Override
	public int compareTo(Fare target) {
		return Integer.compare(this.value, target.value);
	}

	public Fare percentOf(double discountRate) {
		return Fare.from((int)Math.ceil(this.value * discountRate));
	}
}
