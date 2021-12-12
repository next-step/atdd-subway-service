package nextstep.subway.line.domain;

import nextstep.subway.line.exception.money.IllegalMoneyException;

import java.math.BigInteger;
import java.util.Objects;

/**
 * packageName : nextstep.subway.line.domain
 * fileName : Monoy
 * author : haedoang
 * date : 2021/12/12
 * description : 돈 객체 구현. 0원 이상의 금액만 정의한다.
 */
public class Money {
    public static final int MIN_VALUE = 0;
    private BigInteger money;

    private Money(int value) {
        validate(value);
        money = BigInteger.valueOf(value);
    }

    private Money(BigInteger bigInteger) {
        validate(bigInteger.intValue());
        money = bigInteger;
    }

    public static Money of(int value) {
        return new Money(value);
    }

    public static Money of(BigInteger value) {
        return new Money(value);
    }

    private void validate(int value) {
        if (value < MIN_VALUE) {
            throw new IllegalMoneyException();
        }
    }

    public Money minus(Money target) {
        return Money.of(this.money.subtract(target.money));
    }

    public Money plus(Money target) {
        return Money.of(this.money.add(target.money));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money1 = (Money) o;
        return Objects.equals(money, money1.money);
    }

    @Override
    public int hashCode() {
        return Objects.hash(money);
    }
}
