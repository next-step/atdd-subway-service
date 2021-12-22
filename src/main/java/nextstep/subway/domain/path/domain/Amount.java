package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.path.exception.AmountException;
import nextstep.subway.global.error.ErrorCode;

import java.util.Objects;

public class Amount {

    private int amount;

    Amount(int amount) {
        amountValidation(amount, ErrorCode.AMOUNT_NEGATIVE_QUANTITY);
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public Amount plus(Amount amount) {
        amountValidation(amount, ErrorCode.ADDITIONAL_AMOUNT_NEGATIVE_QUANTITY);
        this.amount+=amount.getAmount();
        return new Amount(this.amount);
    }

    public Amount minus(Amount amount) {
        amountValidation(amount, ErrorCode.DISCOUNT_AMOUNT_POSITIVE_QUANTITY);
        this.amount-=amount.getAmount();
        return new Amount(this.amount);
    }

    private void amountValidation(Amount amount, ErrorCode errorCode) {
        if (amount.getAmount() < 0) {
            throw new AmountException(errorCode);
        }
    }

    private void amountValidation(int amount, ErrorCode errorCode) {
        if (amount < 0) {
            throw new AmountException(errorCode);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Amount amount1 = (Amount) o;
        return amount == amount1.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
