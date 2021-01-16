package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class Fare {

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
        int distanceFare = DistancePolicy.getDistanceFare(distance);
        int additionalFare = DistancePolicy.getLineAdditionalFare(path, sections);
        return additionalFare + distanceFare;
    }

    public int getFare() {
        return this.fare;
    }
}
