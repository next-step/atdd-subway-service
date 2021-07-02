package nextstep.subway.path.common;

public class FareOfDistanceUtils {

    private FareOfDistanceUtils() {
        // empty
    }

    public static int getIntervalRateOfDistance(final int distance, final int intervalOfDistance, final int intervalRate) {
        return (int)((Math.ceil((distance - 1) / intervalOfDistance) + 1) * intervalRate);
    }
}
