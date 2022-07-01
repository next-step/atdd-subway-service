package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Fare;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public enum DistanceFarePolicy {

     OVER_FIFTY_KM(50, calculationFunction(8)),
     OVER_TEN_KM(10, calculationFunction(5)),
     DEFAULT(0, distance -> 1250);

     private static final int ADDITIONAL_CHARGE = 100;

     private final Distance distance;
     private final Function<Integer, Integer> calculation;

     DistanceFarePolicy(int distance, Function<Integer, Integer> calculation) {
          this.distance = new Distance(distance);
          this.calculation = calculation;
     }

     private static Function<Integer, Integer> calculationFunction(int criteria) {
          return distanceValue -> (int) ((Math.ceil((distanceValue - 1) / criteria) + 1) * ADDITIONAL_CHARGE);
     }

     public static Fare calculate(Distance distance) {
          int fareValue = 0;
          for (DistanceFarePolicy policy : findMatches(distance)) {
               fareValue += calculate(policy, distance);
               distance = policy.distance;
          }
          return new Fare(fareValue);
     }

     private static List<DistanceFarePolicy> findMatches(Distance distance) {
          return Arrays.stream(values())
                  .filter(value -> distance.isBigger(value.distance))
                  .collect(toList());
     }

     private static int calculate(DistanceFarePolicy policy, Distance distance) {
          return policy.calculation.apply(Distance.minus(distance, policy.distance).getValue());
     }

}
