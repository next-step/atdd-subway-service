package nextstep.subway.path.domain;

import java.util.Objects;

public class Fare {
	static final Fare BASIC = Fare.wonOf(1250);
	static final Fare EXTRA_FARE_UNIT = Fare.wonOf(100);

	private final int amount;

	private Fare(int amount) {
		validateNonNegative(amount);
		this.amount = amount;
	}

	public static Fare wonOf(int amount) {
		return new Fare(amount);
	}

	Fare times(int n) {
		return Fare.wonOf(this.amount * n);
	}

	Fare plus(Fare other) {
		return Fare.wonOf(this.amount + other.amount);
	}

	private void validateNonNegative(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("요금은 음수가 될 수 없다.");
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Fare fare = (Fare)o;
		return amount == fare.amount;
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount);
	}

	public int getAmount() {
		return amount;
	}
}
