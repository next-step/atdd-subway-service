package nextstep.subway.fare;

public class FarePolicy {
    public static int calculateDistanceOverFare(int distance) {
        return DistanceFarePolicy.calculateOverFare(distance);
    }

    public static int calculateDiscountAgeFare(Integer memberAge, int fare) {
        if (memberAge != null) {
            return (int) Math.ceil(DiscountAgeFarePolicy.calculateDiscountFare(memberAge, fare));
        }
        return fare;
    }
}
