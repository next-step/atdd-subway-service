package nextstep.subway.path.domain.fee;

import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.fee.discount.Discount;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static nextstep.subway.path.domain.fee.discount.AgeDiscount.ADULT;

public enum RequireFee {
  DEFAULT_FEE(distance -> distance > 0 && distance <= 10D,
      (distance, discount) -> new BaseCalculatedFee(discount).calculateFee()),
  FIRST_ADDITIONAL_FEE(distance -> distance > 10D && distance <= 50D,
      (distance, discount) -> new FirstAdditionalCalculatedFee(distance, discount).calculateFee()),
  SECOND_ADDITIONAL_FEE(distance -> distance > 50D,
      (distance, discount) -> new SecondAdditionalCalculatedFee(distance, discount).calculateFee());

  private final Predicate<Double> selector;
  private final BiFunction<Double, Discount, Long> calculator;

  RequireFee(Predicate<Double> selector, BiFunction<Double, Discount, Long> calculator) {
    this.selector = selector;
    this.calculator = calculator;
  }

  public static long getRequireFee(Path path) {
    return getRequireFeeWithDiscount(path, ADULT);
  }

  public static long getRequireFeeWithDiscount(Path path, Discount discount) {
    return Arrays.stream(values())
        .filter(feeSelector -> feeSelector.selector.test(path.getDistance()))
        .findFirst()
        .map(feeSelector -> feeSelector.calculator.apply(path.getDistance(), discount))
        .map(calculatedFee -> calculatedFee + path.getAdditionalAmount())
        .orElse(0L);
  }
}
