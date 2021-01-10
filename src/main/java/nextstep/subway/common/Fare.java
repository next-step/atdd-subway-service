package nextstep.subway.common;

import nextstep.subway.line.application.ValidationException;

import java.util.Objects;

public class Fare {
	private final int fare;

	public Fare(int fare) {
		validate(fare);
		this.fare = fare;
	}

	private void validate(int fare) {
		if (fare < 0) {
			throw new ValidationException("fare cannot be low than zero!");
		}
	}

	public Fare plus(Fare fare) {
		return new Fare(this.fare + fare.fare);
	}

	public Fare minus(Fare fare) {
		return new Fare(this.fare - fare.fare);
	}

	public Fare multiply(double multiplier) {
		return new Fare((int) (this.fare * multiplier));
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(fare);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Fare fare1 = (Fare) o;
		return fare == fare1.fare;
	}

	@Override
	public String toString() {
		return "Fare{" +
				"fare=" + fare +
				'}';
	}
}
