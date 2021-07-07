package nextstep.subway.path.domain.common;

public class FareCalculateUtils {

    private FareCalculateUtils() {
        // empty
    }

    public static int getTotalFareOfDistance(final int distance, final int intervalOfDistance, final int intervalRate) {
        return (int)((Math.ceil((distance - 1) / intervalOfDistance) + 1) * intervalRate);
    }

    public static int getFareAfterDiscount(final int fare, final int baseFare, final double discountPercent) {
        int targetFare = fare - baseFare;
        if (targetFare < 0) {
          return fare;
        }
        return (int)(targetFare - Math.ceil(targetFare * discountPercent));
    }
}
