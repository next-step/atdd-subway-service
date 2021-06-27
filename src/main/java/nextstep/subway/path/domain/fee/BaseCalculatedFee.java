package nextstep.subway.path.domain.fee;

import nextstep.subway.path.domain.fee.discount.Discount;

public class BaseCalculatedFee implements CalculatedFee {

  private static final long ADULT_BASE_FEE = 1_250;
  private final Discount discount;

  BaseCalculatedFee(Discount discount) {
    this.discount = discount;
  }

  @Override
  public long calculateFee() {
    return discount.discount(ADULT_BASE_FEE);
  }
}
