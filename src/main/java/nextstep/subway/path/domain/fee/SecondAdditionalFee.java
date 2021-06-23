package nextstep.subway.path.domain.fee;

public class SecondAdditionalFee extends FirstAdditionalFee {

  private static final long DISTANCE_UNTIL_FIRST_ADDITIONAL_FEE = 50L;
  private static final long FEE_ADDITIONAL_DISTANCE = 8L;
  private static final long ADDITIONAL_FEE_UNIT = 100L;

  public SecondAdditionalFee(Double distance) {
    super(distance);
  }

  @Override
  public long calculateFee() {
    return super.calculateFee() + (long) Math.ceil((distance - DISTANCE_UNTIL_FIRST_ADDITIONAL_FEE) / FEE_ADDITIONAL_DISTANCE) * ADDITIONAL_FEE_UNIT;
  }
}
