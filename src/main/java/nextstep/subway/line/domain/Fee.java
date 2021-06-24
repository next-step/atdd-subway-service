package nextstep.subway.line.domain;

import nextstep.subway.exception.InvalidFeeAmountException;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Fee {

  public static final Fee ZERO_FEE = new Fee(0);

  private Long amount;

  protected Fee() {
  }

  private Fee(long amount) {
    this.amount = amount;
  }

  public static Fee from(long amount) {
    if (amount < 0) {
      throw new InvalidFeeAmountException();
    }
    return new Fee(amount);
  }

  public Long getAmount() {
    return amount;
  }

  public void setAmount(Long amount) {
    this.amount = amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Fee fee = (Fee) o;
    return Objects.equals(amount, fee.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount);
  }
}
