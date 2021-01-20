package nextstep.subway.fare;


public enum FareDistanceRule {
    추가운임_50km_이내(5, 100),
    추가운임_50km_초과(8, 100);

    private static final long DEFAULT_FARE = 1250;
    private static final int DISTANCE_10KM = 10;
    private static final int DISTANCE_50KM = 50;

    private final int fareDistance;
    private final long surcharge;

    FareDistanceRule(int fareDistance, long surcharge) {
        this.fareDistance = fareDistance;
        this.surcharge = surcharge;
    }

    public int getFareDistance() {
        return fareDistance;
    }

    public long getSurcharge() {
        return surcharge;
    }

    public static long findFareByDistance(int distance) {
        long fare = DEFAULT_FARE;

        if (distance > DISTANCE_50KM) {
            fare += findSurcharge(추가운임_50km_초과, distance - DISTANCE_50KM);
            distance = DISTANCE_50KM;
        }

        if (distance > DISTANCE_10KM) {
            fare += findSurcharge(추가운임_50km_이내, distance - DISTANCE_10KM);
        }

        return fare;
    }

    private static long findSurcharge(FareDistanceRule fareDistanceRule, int distance) {
        int share = distance / fareDistanceRule.getFareDistance();
        share += distance % fareDistanceRule.getFareDistance() == 0 ? 0 : 1;
        long surcharge = share * fareDistanceRule.getSurcharge();
        return surcharge;
    }
}
