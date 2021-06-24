package nextstep.subway.path.domain.fee;

public class BaseFee implements Fee {

  private static final long ADULT_BASE_FEE = 1_250;

  BaseFee() {

  }

  @Override
  public long calculateFee() {
    return ADULT_BASE_FEE;
  }
}
