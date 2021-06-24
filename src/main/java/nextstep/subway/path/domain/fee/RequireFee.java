package nextstep.subway.path.domain.fee;

import nextstep.subway.line.domain.Fee;
import nextstep.subway.path.domain.Path;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public enum RequireFee {
  DEFAULT_FEE(distance -> distance > 0 && distance <= 10D,
      (distance, fee) -> new BaseCalculatedFee(fee).calculateFee()),
  FIRST_ADDITIONAL_FEE(distance -> distance > 10D && distance <= 50D,
      (distance, fee) -> new FirstAdditionalCalculatedFee(distance, fee).calculateFee()),
  SECOND_ADDITIONAL_FEE(distance -> distance > 50D,
      (distance, fee) -> new SecondAdditionalCalculatedFee(distance, fee).calculateFee());

  private final Predicate<Double> selector;
  private final BiFunction<Double, Fee, Long> calculator;

  RequireFee(Predicate<Double> selector, BiFunction<Double, Fee, Long> calculator) {
    this.selector = selector;
    this.calculator = calculator;
  }

  public static long getRequireFee(Path path) {
    return Arrays.stream(values())
        .filter(feeSelector -> feeSelector.selector.test(path.getDistance()))
        .findFirst()
        .map(feeSelector -> feeSelector.calculator.apply(path.getDistance(), path.getPathAdditionalFee()))
        .orElse(0L);
  }
}
