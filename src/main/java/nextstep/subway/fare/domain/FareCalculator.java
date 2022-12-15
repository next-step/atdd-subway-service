package nextstep.subway.fare.domain;

public class FareCalculator {
    public static int calculate(int pathDistance, int maxLineFare, int age) {
        return Fare.of(DiscountPolicy.getPolicy(age))
            .plus(DistanceExtraFare.calculate(pathDistance))
            .plus(maxLineFare)
            .discount();
    }
}
