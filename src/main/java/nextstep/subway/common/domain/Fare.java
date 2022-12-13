package nextstep.subway.common.domain;

import javax.persistence.Embeddable;

import io.jsonwebtoken.lang.Assert;

@Embeddable
public class Fare {

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
}
