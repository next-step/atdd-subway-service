package nextstep.subway.line.domain.vo;

import nextstep.subway.exception.InvalidFeeAmountException;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Amount {

  public static final Amount ZERO_AMOUNT = new Amount(0);

  private Long amount;

  protected Amount() {
  }

  private Amount(long amount) {
    this.amount = amount;
  }

  public static Amount from(long amount) {
    if (amount < 0) {
      throw new InvalidFeeAmountException();
    }
    return new Amount(amount);
  }

  public Long getAmount() {
    return amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Amount amount = (Amount) o;
    return Objects.equals(this.amount, amount.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount);
  }
}
