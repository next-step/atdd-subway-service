package nextstep.subway.common.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import io.jsonwebtoken.lang.Assert;

@Embeddable
public class Age {

	private static final int ZERO = 0;
	@Column(name = "age", nullable = false)
	private int value;

	protected Age() {
	}

	private Age(int value) {
		Assert.isTrue(positive(value), "나이는 0보다 작을 수 없습니다.");
		this.value = value;
	}

	public static Age from(int value) {
		return new Age(value);
	}

	public int value() {
		return value;
	}

	private boolean positive(int value) {
		return value >= ZERO;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Age age = (Age)o;
		return value == age.value;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
