package nextstep.subway.member.domain;

import javax.persistence.Embeddable;

import nextstep.subway.exception.AppException;
import nextstep.subway.exception.ErrorCode;

@Embeddable
public class Age {

	public static final int MINIMUM = 0;

	private final int age;

	protected Age() {
		this.age = MINIMUM;
	}

	private Age(int age) {
		this.age = age;
	}

	public static Age of() {
		return new Age(MINIMUM);
	}

	public static Age of(int age) {
		validate(age);
		return new Age(age);
	}

	private static void validate(int age) {
		if (age < MINIMUM) {
			throw new AppException(ErrorCode.WRONG_INPUT, "최소값 {} 보다 커야 합니다", MINIMUM);
		}
	}

	public boolean isEqualsAndBetween(int minimum, int maximum) {
		return age >= minimum && age <= maximum;
	}

	public int toInt() {
		return this.age;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Age age1 = (Age)o;

		return age == age1.age;
	}

	@Override
	public int hashCode() {
		return age;
	}
}
