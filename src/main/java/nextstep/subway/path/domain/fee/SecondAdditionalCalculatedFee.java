package nextstep.subway.path.domain.fee;

import nextstep.subway.line.domain.Fee;

public class SecondAdditionalCalculatedFee implements CalculatedFee {

  private static final double DISTANCE_UNTIL_FIRST_ADDITIONAL_FEE = 50D;
  private static final long FEE_ADDITIONAL_DISTANCE = 8L;
  private static final long ADDITIONAL_FEE_UNIT = 100L;

  private final FirstAdditionalCalculatedFee firstAdditionalFee;
  private final Double secondAdditionalSectionDistance;

  SecondAdditionalCalculatedFee(Double distance, Fee pathAdditionalFee) {
    if (distance <= DISTANCE_UNTIL_FIRST_ADDITIONAL_FEE) {
      throw new IllegalArgumentException("");
    }
    this.firstAdditionalFee = new FirstAdditionalCalculatedFee(DISTANCE_UNTIL_FIRST_ADDITIONAL_FEE, pathAdditionalFee);
    this.secondAdditionalSectionDistance = distance - DISTANCE_UNTIL_FIRST_ADDITIONAL_FEE;
  }

  @Override
  public long calculateFee() {
    return firstAdditionalFee.calculateFee() + (long) Math.ceil((secondAdditionalSectionDistance) / FEE_ADDITIONAL_DISTANCE) * ADDITIONAL_FEE_UNIT;
  }
}
