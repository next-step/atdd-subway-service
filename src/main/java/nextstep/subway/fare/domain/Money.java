package nextstep.subway.fare.domain;

import java.util.Objects;

import lombok.Getter;

@Getter
public class Money {

	private final long money;

	public Money(long money) {
		validateMoney(money);
		this.money = money;
	}

	public static Money of(long amount) {
		return new Money(amount);
	}

	private void validateMoney(long money) {
		if (money < 0L) {
			throw new IllegalArgumentException("돈은 0보다 작을 수 없습니다.");
		}
	}

	public Money plus(int amount) {
		return new Money(this.getMoney() + amount);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Money money1 = (Money)o;
		return money == money1.money;
	}

	@Override
	public int hashCode() {
		return Objects.hash(money);
	}
}
