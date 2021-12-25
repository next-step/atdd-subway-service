package nextstep.subway.fare.domain;

import javax.persistence.Embeddable;

import nextstep.subway.exception.AppException;
import nextstep.subway.exception.ErrorCode;

@Embeddable
public class Fare {

	private static final int MINIMUM = 0;

	private final int fare;

	protected Fare() {
		this.fare = MINIMUM;
	}

	private Fare(int fare) {
		this.fare = fare;
	}

	public static Fare of() {
		return new Fare();
	}

	public static Fare zero() {
		return new Fare(0);
	}

	public static Fare of(int fareNumber) {
		return new Fare(fareNumber);
	}

	public void validate(int fare) {
		if (fare < MINIMUM) {
			throw new AppException(ErrorCode.WRONG_INPUT, "최소 {} 이상이어야 합니다", MINIMUM);
		}
	}

	public int toInt() {
		return this.fare;
	}

	public Fare add(Fare other) {
		return Fare.of(this.fare + other.fare);
	}

	public boolean isZero() {
		return this.fare == 0;
	}

	public int compareTo(Fare other) {
		return this.fare - other.fare;
	}

	public Fare discountByPercent(int percentage) {
		return Fare.of((int)((100 - percentage) / 100D * fare));
	}

	@Override
	public String toString() {
		return "Fare{" +
			"fare=" + fare +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Fare fare1 = (Fare)o;

		return fare == fare1.fare;
	}

	@Override
	public int hashCode() {
		return fare;
	}
}
