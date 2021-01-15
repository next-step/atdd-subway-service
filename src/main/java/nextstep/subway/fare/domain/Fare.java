package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class Fare {

    private static final int BASIC_DISTANCE = 10;
    private static final int OVER_DISTANCE = 50;
    private static final int BASIC_FARE = 1_250;
    public static final int OVER_FARE_STANDARD_1 = 5;
    public static final int OVER_FARE_STANDARD_2 = 8;

    private int fare;
    private AgePolicy agePolicy;

    public Fare() {
        this.agePolicy = AgePolicy.STANDARD;
    }

    public Fare(AgePolicy agePolicy) {
        this.agePolicy = agePolicy;
    }

    public void calculateFare(List<Station> path, List<Section> sections, int distance) {
        int standardFare = calculateStandardFare(path, sections, distance);
        this.fare = (standardFare - agePolicy.getDeductionAmount()) * agePolicy.getDiscountRate() / 100;
    }

    private int calculateStandardFare(List<Station> path, List<Section> sections, int distance) {
        int additionalFare = calculateAdditionalFare(path, sections);

        if (distance <= BASIC_DISTANCE) {
            return BASIC_FARE + additionalFare;
        }

        if (distance <= OVER_DISTANCE) {
            return BASIC_FARE + calculateOverFare(distance - BASIC_DISTANCE, OVER_FARE_STANDARD_1) + additionalFare;
        }

        return BASIC_FARE + calculateOverFare(OVER_DISTANCE - BASIC_DISTANCE, OVER_FARE_STANDARD_1) +
                calculateOverFare(distance - OVER_DISTANCE, OVER_FARE_STANDARD_2) + additionalFare;
    }

    private int calculateOverFare(int distance, int eachStandard) {
        return (int) ((Math.ceil((distance - 1) / eachStandard) + 1) * 100);
    }

    private int calculateAdditionalFare(List<Station> path, List<Section> sections) {
        int maxFare = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            int currentFare = getMatchedAdditionalLineFare(sections, path.get(i), path.get(i + 1));
            maxFare = Math.max(maxFare, currentFare);
        }
        return maxFare;
    }

    private int getMatchedAdditionalLineFare(List<Section> sections, Station upStation, Station downStation) {
        return sections.stream()
                .filter(section -> section.isEqualWithUpStation(upStation) && section.isEqualWithDownStation(downStation))
                .map(Section::getAdditionalFare)
                .findFirst()
                .orElse(0);
    }

    public int getFare() {
        return this.fare;
    }
}
