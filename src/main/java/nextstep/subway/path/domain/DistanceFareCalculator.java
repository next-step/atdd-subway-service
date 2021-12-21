package nextstep.subway.path.domain;

public class DistanceFareCalculator {
    private static final int ADDITIONAL_FARE = 100;
    private static final int FIVE_KM = 5;
    private static final int EIGHT_KM = 8;
    private static final int TEN = 10;
    private static final int FIFTY = 50;

    public static Fare calculateByDistance(double totalDistance) {
        if (moreTenLessFiftyDistance(totalDistance)) {
            return new Fare(calculateOverFare(totalDistance - TEN, FIVE_KM));
        }

        if (moreFiftyDistance(totalDistance)) {
            return new Fare(calculateOverFare(FIFTY - TEN, FIVE_KM) + calculateOverFare(totalDistance - FIFTY, EIGHT_KM));
        }

        return new Fare(0);
    }

    private static boolean moreFiftyDistance(double totalDistance) {
        return totalDistance > 50;
    }

    private static boolean moreTenLessFiftyDistance(double totalDistance) {
        return TEN < totalDistance && totalDistance <= FIFTY;
    }

    private static int calculateOverFare(double distance, int perKm) {
        return (int) ((Math.floor((distance) / perKm)) * ADDITIONAL_FARE);
    }

}
