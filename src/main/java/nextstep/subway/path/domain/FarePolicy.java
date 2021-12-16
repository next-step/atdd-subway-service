package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.ListIterator;
import java.util.stream.Collectors;

public enum FarePolicy {
  DEFAULT(10, 1250, 0),
  TO_FIFTY(50, 100, 5),
  OVER(Integer.MAX_VALUE, 100, 8);

  private final int distance;
  private final int fare;
  private final int chargeSection;

  FarePolicy(int distance, int fare, int chargeSection) {
    this.distance = distance;
    this.fare = fare;
    this.chargeSection = chargeSection;
  }

  public static int getTotalFare(int distance) {
    ListIterator<FarePolicy> farePolicyListIterator = Arrays.stream(FarePolicy.values())
            .collect(Collectors.toList())
            .listIterator();

    return calculateTotalFare(distance, farePolicyListIterator);
  }

  private static int calculateTotalFare(int distance, ListIterator<FarePolicy> farePolicyListIterator) {
    int totalFare = 0;
    while (farePolicyListIterator.hasNext()) {
      int previousDistance = getPreviousDistance(farePolicyListIterator);
      FarePolicy farePolicy = farePolicyListIterator.next();
      int distanceRange = farePolicy.distance - previousDistance;
      int calculateDistance = Math.min(distance, distanceRange);
      distance -= calculateDistance;
      totalFare += calculateFare(farePolicy, calculateDistance);
    }
    return totalFare;
  }

  private static int getPreviousDistance(ListIterator<FarePolicy> farePolicyIterator) {
    int previousDistance = 0;
    if (farePolicyIterator.hasPrevious()) {
      previousDistance = farePolicyIterator.previous().distance;
      farePolicyIterator.next();
    }
    return previousDistance;
  }

  private static int calculateFare(FarePolicy policy, int distance) {
    if (policy.equals(DEFAULT)) {
      return policy.fare;
    }

    if (distance <= 0) {
      return 0;
    }

    return (int) ((Math.ceil((distance - 1) / policy.chargeSection) + 1) * policy.fare);
  }
}
