package nextstep.subway.path.domain.fee;

import nextstep.subway.path.domain.fee.discount.Discount;

public class FirstAdditionalCalculatedFee implements CalculatedFee {

  private static final double DISTANCE_UNTIL_BASE_FEE = 10D;
  private static final long FEE_ADDITIONAL_DISTANCE = 5L;
  private static final long ADDITIONAL_FEE_UNIT = 100L;

  private final BaseCalculatedFee baseFee;
  private final Double firstAdditionalSectionDistance;
  private final Discount discount;

  FirstAdditionalCalculatedFee(Double distance, Discount discount) {
    if (distance <= DISTANCE_UNTIL_BASE_FEE) {
      throw new IllegalArgumentException("");
    }
    this.baseFee = new BaseCalculatedFee(discount);
    this.firstAdditionalSectionDistance = distance - DISTANCE_UNTIL_BASE_FEE;
    this.discount = discount;
  }

  @Override
  public long calculateFee() {
    return baseFee.calculateFee() + (long) Math.ceil((firstAdditionalSectionDistance) / FEE_ADDITIONAL_DISTANCE) * discount.discount(ADDITIONAL_FEE_UNIT, this.getClass());
  }
}
