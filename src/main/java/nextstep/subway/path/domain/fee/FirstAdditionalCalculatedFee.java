package nextstep.subway.path.domain.fee;

import nextstep.subway.line.domain.Fee;

public class FirstAdditionalCalculatedFee implements CalculatedFee {

  private static final double DISTANCE_UNTIL_BASE_FEE = 10D;
  private static final long FEE_ADDITIONAL_DISTANCE = 5L;
  private static final long ADDITIONAL_FEE_UNIT = 100L;

  private final BaseCalculatedFee baseFee;
  private final Double firstAdditionalSectionDistance;

  FirstAdditionalCalculatedFee(Double distance, Fee pathAdditionalFee) {
    if (distance <= DISTANCE_UNTIL_BASE_FEE) {
      throw new IllegalArgumentException("");
    }
    this.baseFee = new BaseCalculatedFee(pathAdditionalFee);
    this.firstAdditionalSectionDistance = distance - DISTANCE_UNTIL_BASE_FEE;
  }

  @Override
  public long calculateFee() {
    return baseFee.calculateFee() + (long) Math.ceil((firstAdditionalSectionDistance) / FEE_ADDITIONAL_DISTANCE) * ADDITIONAL_FEE_UNIT;
  }
}
