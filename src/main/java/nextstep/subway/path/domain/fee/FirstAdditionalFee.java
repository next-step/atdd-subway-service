package nextstep.subway.path.domain.fee;

public class FirstAdditionalFee extends BaseFee {

  private static final double CALCULATE_LIMIT_DISTANCE = 50D;
  private static final long DISTANCE_UNTIL_BASE_FEE = 10L;
  private static final long FEE_ADDITIONAL_DISTANCE = 5L;
  private static final long ADDITIONAL_FEE_UNIT = 100L;

  public FirstAdditionalFee(Double distance) {
    super(distance);
  }

  @Override
  public long calculateFee() {
    double calculatingBaseDistance = distance;
    if (distance > CALCULATE_LIMIT_DISTANCE) {
      calculatingBaseDistance = CALCULATE_LIMIT_DISTANCE;
    }
    return super.calculateFee() + (long) Math.ceil((calculatingBaseDistance - DISTANCE_UNTIL_BASE_FEE) / FEE_ADDITIONAL_DISTANCE) * ADDITIONAL_FEE_UNIT;
  }
}
