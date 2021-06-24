package nextstep.subway.path.domain.fee;

import nextstep.subway.path.domain.fee.discount.Discount;

public class SecondAdditionalCalculatedFee implements CalculatedFee {

  private static final double DISTANCE_UNTIL_FIRST_ADDITIONAL_FEE = 50D;
  private static final long FEE_ADDITIONAL_DISTANCE = 8L;
  private static final long ADDITIONAL_FEE_UNIT = 100L;

  private final FirstAdditionalCalculatedFee firstAdditionalFee;
  private final Double secondAdditionalSectionDistance;
  private final Discount discount;

  SecondAdditionalCalculatedFee(Double distance, Discount discount) {
    if (distance <= DISTANCE_UNTIL_FIRST_ADDITIONAL_FEE) {
      throw new IllegalArgumentException("");
    }
    this.firstAdditionalFee = new FirstAdditionalCalculatedFee(DISTANCE_UNTIL_FIRST_ADDITIONAL_FEE, discount);
    this.secondAdditionalSectionDistance = distance - DISTANCE_UNTIL_FIRST_ADDITIONAL_FEE;
    this.discount = discount;
  }

  @Override
  public long calculateFee() {
    return firstAdditionalFee.calculateFee() + (long) Math.ceil((secondAdditionalSectionDistance) / FEE_ADDITIONAL_DISTANCE) * discount.discount(ADDITIONAL_FEE_UNIT, this.getClass());
  }
}
