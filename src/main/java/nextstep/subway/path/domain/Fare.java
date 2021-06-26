package nextstep.subway.path.domain;

public class Fare {
    private static final int BASE_FARE = 1250;

    private int distance;
    private int addMaxFare;

    private double discountRate;

    private Fare(int distance, int addMaxFare, double discountRate) {
        this.distance = distance;
        this.addMaxFare = addMaxFare;
        this.discountRate = discountRate;
    }

    public static Fare of(int distance) {
        return new Fare(distance, 0, 1L);
    }

    public static Fare of(int distance, int addMaxFare) {
        return new Fare(distance, addMaxFare, 1L);
    }

    public static Fare of(int distance, int addMaxFare, double discountRate) {
        return new Fare(distance, addMaxFare, discountRate);
    }

    public int getDistance() {
        return distance;
    }

    public int getPrice() {
        int fare = BASE_FARE + addMaxFare;
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

        return (int)Math.ceil(fare * discountRate);
    }

    private int calculateOverFare(int distance, int divisor, int addFare) {
        return (int)((Math.ceil((distance) / divisor)) * addFare);
    }

}
