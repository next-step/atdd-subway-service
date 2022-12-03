package nextstep.subway.line.domain;

import java.util.Arrays;

public enum DistanceFarePolicy {

    FIFTY_KM(50, Distance.from(8), Fare.from(100), Fare.from(800)),
    TEN_KM(10, Distance.from(5), Fare.from(100), Fare.from(0)),
    ZERO_KM(0, Distance.from(10), Fare.from(0), Fare.from(0))
    ;

    private int minKm;
    private Distance interval;
    private Fare intervalFare;
    private Fare basicAdditionalFare;

    DistanceFarePolicy(int minKm, Distance interval, Fare intervalFare, Fare basicAdditionalFare) {
        this.minKm = minKm;
        this.interval = interval;
        this.intervalFare = intervalFare;
        this.basicAdditionalFare = basicAdditionalFare;
    }

    public static DistanceFarePolicy findDistanceFarePolicy(Distance distance) {
        return Arrays.stream(DistanceFarePolicy.values())
                .filter(distanceFarePolicy -> distance.isBiggerThen(distanceFarePolicy.minKm))
                .findFirst()
                .orElse(ZERO_KM);
    }

    public Fare calculateAdditionalFareOfDistance(Distance distance) {
        return intervalFare.multiply(distance.subtract(minKm).divideAndCeil(interval))
                .add(basicAdditionalFare);
    }
}
