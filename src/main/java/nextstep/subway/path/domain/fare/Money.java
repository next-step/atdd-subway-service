package nextstep.subway.path.domain.fare;

import javax.persistence.Embeddable;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
@Embeddable
public class Money {

	private static final long ZERO_MONEY = 0L;
	private final long money;
	
	public static final Money ZERO = Money.of(ZERO_MONEY);

	public Money() {
		this.money = ZERO_MONEY;
	}

	public Money(long money) {
		validateMoney(money);
		this.money = money;
	}

	public static Money of(long amount) {
		return new Money(amount);
	}

	private void validateMoney(long money) {
		if (money < ZERO_MONEY) {
			throw new IllegalArgumentException("돈은 0보다 작을 수 없습니다.");
		}
	}

	public Money plus(int amount) {
		return Money.of(this.getMoney() + amount);
	}

	public Money plus(Money amount) {
		return Money.of(this.money + amount.getMoney());
	}

	public Money multi(double discount) {
		return Money.of((int)(this.money * discount));
	}
}
