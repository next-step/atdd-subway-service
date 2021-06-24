package nextstep.subway.path.domain.fee;

public class BaseCalculatedFee implements CalculatedFee {

  private static final long ADULT_BASE_FEE = 1_250;

  BaseCalculatedFee() {

  }

  @Override
  public long calculateFee() {
    return ADULT_BASE_FEE;
  }
}
