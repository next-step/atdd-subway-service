package nextstep.subway.line.domain;


public class Fare {

    private static final int DEFAULT_FARE = 1250;
    private static final int DEFAULT_EXTRA_FARE = 0;
    private static final int STANDARD_LONG_DISTANCE = 50;
    private static final int STANDARD_SHORT_DISTANCE = 10;

    private int defaultFare;
    private int distance;
    private int extraFare;

    public Fare(int distance) {
        this(distance, DEFAULT_EXTRA_FARE);
    }

    public Fare(int distance, int extraFare) {
        this(distance, extraFare, DEFAULT_FARE);
    }

    public Fare(int distance, int extraFare, int defaultFare) {
        this.distance = distance;
        this.extraFare = extraFare;
        this.defaultFare = defaultFare;
    }

    public int getFare() {
        defaultFare += calculateOverFare();
        defaultFare += extraFare;

        return defaultFare;
    }

    private int calculateOverFare() {
        int fare = 0;

        int remainDistance = distance;
        int sectionDistance = 0;
        DistanceFareUnit[] distanceFareUnits = DistanceFareUnit.values();
        for (DistanceFareUnit distanceFareUnit : distanceFareUnits) {
            sectionDistance = getCorrectionUnit(remainDistance - distanceFareUnit.ofBoundary()); //

            fare += distanceFareUnit.fare(sectionDistance);   // 0

            remainDistance -= sectionDistance;
        }
        return fare;
    }

    private int getCorrectionUnit(int sectionDistance) {
        if(sectionDistance < 0) {
            sectionDistance = 0;
        }
        return sectionDistance;
    }
}
