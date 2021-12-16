package nextstep.subway.path.domain;

import java.util.Arrays;

public enum FareDiscountPolicy {
  CHILD(6, 12, 0.5),
  YOUTH(13, 18, 0.2);

  private static final int DEDUCTION = 350;

  private final int minAge;
  private final int maxAge;
  private final double discountRate;

  FareDiscountPolicy(int minAge, int maxAge, double discountRate) {
    this.minAge = minAge;
    this.maxAge = maxAge;
    this.discountRate = discountRate;
  }

  public static int getDiscountFare(int age, int fare) {
    return Arrays.stream(values())
            .filter(policy -> age >= policy.minAge && age <= policy.maxAge)
            .findFirst()
            .map(policy -> calculateDiscountFare(fare, policy.discountRate))
            .orElse(fare);
  }

  private static int calculateDiscountFare(int fare, double discountRate) {
    return (int) ((fare - DEDUCTION) * (1 - discountRate));
  }
}
