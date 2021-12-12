package nextstep.subway.line.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nextstep.subway.line.exception.money.IllegalMoneyException;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.math.BigInteger;
import java.util.Objects;

/**
 * packageName : nextstep.subway.line.domain
 * fileName : Monoy
 * author : haedoang
 * date : 2021/12/12
 * description : 돈 객체 구현. 0원 이상의 금액만 정의한다.
 */
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Money {
    @Transient
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

    public int intValue() {
        return money.intValue();
    }

    public boolean exist() {
        return money.intValue() > MIN_VALUE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money1 = (Money) o;
        return Objects.equals(money.intValue(), money1.money.intValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(money);
    }

    public Money discount(int deductibleAmount, int discountRate) {
        return Money.of((money.intValue() - deductibleAmount) * (100 - discountRate) / 100);
    }
}
