package nextstep.subway.path.domain.fee;

import nextstep.subway.line.domain.Fee;

public class BaseCalculatedFee implements CalculatedFee {

  private static final long ADULT_BASE_FEE = 1_250;

  private final Fee additionalFee;

  BaseCalculatedFee(Fee pathAdditionalFee) {
    this.additionalFee = pathAdditionalFee;
  }

  @Override
  public long calculateFee() {
    return ADULT_BASE_FEE + additionalFee.getAmount();
  }
}
