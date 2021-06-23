package nextstep.subway.path.domain.fee;

public class BaseFee {

  private static final long ADULT_BASE_FEE = 1_250;

  protected final Double distance;

  public BaseFee(Double distance) {
    this.distance = distance;
  }

  public long calculateFee() {
    return ADULT_BASE_FEE;
  }
}
