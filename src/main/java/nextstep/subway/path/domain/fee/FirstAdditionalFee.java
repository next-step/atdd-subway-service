package nextstep.subway.path.domain.fee;

public class FirstAdditionalFee implements Fee {

  private static final double DISTANCE_UNTIL_BASE_FEE = 10D;
  private static final long FEE_ADDITIONAL_DISTANCE = 5L;
  private static final long ADDITIONAL_FEE_UNIT = 100L;

  private final BaseFee baseFee;
  private final Double firstAdditionalSectionDistance;

  FirstAdditionalFee(Double distance) {
    if (distance <= DISTANCE_UNTIL_BASE_FEE) {
      throw new IllegalArgumentException("");
    }
    this.baseFee = new BaseFee();
    this.firstAdditionalSectionDistance = distance - DISTANCE_UNTIL_BASE_FEE;
  }

  @Override
  public long calculateFee() {
    return baseFee.calculateFee() + (long) Math.ceil((firstAdditionalSectionDistance) / FEE_ADDITIONAL_DISTANCE) * ADDITIONAL_FEE_UNIT;
  }
}
