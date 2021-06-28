package nextstep.subway.path.domain.fee.discount;

import java.util.Arrays;
import java.util.function.LongUnaryOperator;
import java.util.function.Predicate;

public enum AgeDiscount implements Discount {

  TODDLER(age -> age >= 0 && age < 6,
          fee -> 0),
  CHILDREN(age -> age >= 6 && age < 13,
          fee -> (long) ((fee - 350) * 0.5)),
  TEENAGER(age -> age >= 13 && age < 19,
          fee -> (long) ((fee - 350) * 0.8)),
  ADULT(age -> age >= 19,
          fee -> fee);

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
  public Long discount(Long fee) {
    return this.discountCalculator.applyAsLong(fee);
  }
}
