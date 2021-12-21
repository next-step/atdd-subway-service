package nextstep.subway.path.domain;

public class FareCalculator {
    private static final int ADDITIONAL_FARE = 100;
    private static final int FIVE_KM = 5;
    private static final int EIGHT_KM = 8;
    private static final int TEN = 10;
    private static final int FIFTY = 50;

    public static Fare calculate(double totalDistance) {
        if (moreTenLessFiftyDistance(totalDistance)) {
            int fare = Fare.getTotalFare(calculateOverFare(totalDistance - TEN, FIVE_KM)).getFare();
            return Fare.getTotalFare(calculateOverFare(totalDistance - TEN, FIVE_KM));
        }

        if (moreFiftyDistance(totalDistance)) {
            return Fare.getTotalFare(calculateOverFare(FIFTY-TEN, FIVE_KM) + calculateOverFare(totalDistance - FIFTY, EIGHT_KM));
        }

        return Fare.getDefaultFare();
    }

    private static boolean moreFiftyDistance(double totalDistance) {
        return totalDistance > 50;
    }

    private static boolean moreTenLessFiftyDistance(double totalDistance) {
        return TEN < totalDistance && totalDistance <= FIFTY;
    }

    private static int calculateOverFare(double distance, int perKm) {
        return (int) ((Math.floor((distance) / perKm) ) * ADDITIONAL_FARE);
    }

}
