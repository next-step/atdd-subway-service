package nextstep.subway.path.domain.fee.discount;

import nextstep.subway.path.domain.fee.BaseCalculatedFee;
import nextstep.subway.path.domain.fee.CalculatedFee;

import java.util.Arrays;
import java.util.function.LongUnaryOperator;
import java.util.function.Predicate;

public enum AgeDiscount implements Discount {

  TODDLER(age -> age >= 0 && age < 6,
          fee -> 0),
  CHILDREN(age -> age >= 6 && age < 13,
          fee -> (long) (fee * 0.5)),
  TEENAGER(age -> age >= 13 && age < 19,
          fee -> (long) (fee * 0.8)),
  ADULT(age -> age >= 19,
          fee -> fee);

  private static final long DEDUCTION_BASE_FEE = 350;

  private final Predicate<Integer> selector;
  private final LongUnaryOperator discountCalculator;

  AgeDiscount(Predicate<Integer> selector, LongUnaryOperator discountCalculator) {
    this.selector = selector;
    this.discountCalculator = discountCalculator;
  }

  public static Discount from(Integer age) {
    return Arrays.stream(values())
            .filter(ageDiscount -> ageDiscount.selector.test(age))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
  }

  @Override
  public Long discount(Long fee, Class<? extends CalculatedFee> clazz) {
    if (BaseCalculatedFee.class.equals(clazz)) {
      return this.discountCalculator.applyAsLong(deductFee(fee));
    }
    return this.discountCalculator.applyAsLong(fee);
  }

  private Long deductFee(Long fee) {
    if (this != ADULT) {
      return fee - DEDUCTION_BASE_FEE;
    }
    return fee;
  }
}
