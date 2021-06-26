package nextstep.subway.line.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Fare implements Comparable<Fare> {
	public static final Fare ZERO = Fare.wonOf(0);
	public static final Fare YOUNG_DEFAULT_DISCOUNT = Fare.wonOf(350);

	@Column
	private int amount;

	protected Fare() {}

	private Fare(int amount) {
		validateNonNegative(amount);
		this.amount = amount;
	}

	public static Fare wonOf(int amount) {
		return new Fare(amount);
	}

	public Fare times(double n) {
		return Fare.wonOf((int)(this.amount * n));
	}

	public Fare plus(Fare other) {
		return Fare.wonOf(this.amount + other.amount);
	}

	public Fare minus(Fare other) {
		return Fare.wonOf(this.amount - other.amount);
	}

	public int getAmount() {
		return amount;
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

	@Override
	public int compareTo(Fare other) {
		return Integer.compare(this.amount, other.amount);
	}

	@Override
	public String toString() {
		return "Fare{" +
			"amount=" + amount +
			'}';
	}
}
