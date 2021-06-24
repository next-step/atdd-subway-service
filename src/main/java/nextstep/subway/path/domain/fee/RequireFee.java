package nextstep.subway.path.domain.fee;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum RequireFee {
  DEFAULT_FEE(distance -> distance > 0 && distance <= 10D,
      distance -> new BaseCalculatedFee().calculateFee()),
  FIRST_ADDITIONAL_FEE(distance -> distance > 10D && distance <= 50D,
      distance -> new FirstAdditionalCalculatedFee(distance).calculateFee()),
  SECOND_ADDITIONAL_FEE(distance -> distance > 50D,
      distance -> new SecondAdditionalCalculatedFee(distance).calculateFee());

  private final Predicate<Double> selector;
  private final Function<Double, Long> calculator;

  RequireFee(Predicate<Double> selector, Function<Double, Long> calculator) {
    this.selector = selector;
    this.calculator = calculator;
  }

  public static long getRequireFee(double distance) {
    return Arrays.stream(values())
        .filter(feeSelector -> feeSelector.selector.test(distance))
        .findFirst()
        .map(feeSelector -> feeSelector.calculator.apply(distance))
        .orElse(0L);
  }
}
