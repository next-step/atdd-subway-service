package nextstep.subway.path.domain;

public class Fare {
    private static final int BASE_FARE = 1250;

    private int distance;

    private Fare(int distance) {
        this.distance = distance;
    }

    public static Fare of(int distance) {
        return new Fare(distance);
    }

    public int getDistance() {
        return distance;
    }

    public int getPrice() {
        int fare = BASE_FARE;
        int firstDistance = distance;
        Surcharge surcharge = Surcharge.findSurchargeByDistance(firstDistance);
        while (surcharge != Surcharge.NONE) {
            int distanceBoundary = surcharge.getDistanceBoundary();
            int addDistance = surcharge.getAddDistance();
            int addFare = surcharge.getAddFare();
            fare += calculateOverFare(firstDistance - distanceBoundary, addDistance, addFare);
            firstDistance = distanceBoundary;
            surcharge = Surcharge.findSurchargeByDistance(firstDistance);
        }

        return fare;
    }

    private int calculateOverFare(int distance, int divisor, int addFare) {
        return (int)((Math.ceil((distance) / divisor)) * addFare);
    }

}
