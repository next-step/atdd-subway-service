package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;

import java.util.List;

public class Fare {
    public static final int DEFAULT_FARE = 1250;

    private final int fare;

    private Fare(int fare) {
        this.fare = fare;
    }

    public static Fare of(int distance) {
        int chargeByDistance = DistanceChargeRule.calculateChargeByDistance(distance);
        return new Fare(DEFAULT_FARE + chargeByDistance);
    }

    public static Fare of(int distance, List<Section> sections) {
        int chargeByDistance = DistanceChargeRule.calculateChargeByDistance(distance);
        int chargeByLine = getMaxSurchargeByLine(sections);
        return new Fare(DEFAULT_FARE + chargeByDistance + chargeByLine);
    }

    private static int getMaxSurchargeByLine(List<Section> sections) {
        return sections.stream()
                .mapToInt(it -> it.getLine().getSurcharge())
                .max()
                .orElse(0);
    }

    public int getFare() {
        return fare;
    }
}
