package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Line;

public class FareCalculator {
    private static final int BASE_FARE = 1250;
    private Line line;
    private DistanceFare distanceFare;
    private AgePolicy agePolicy;


    public FareCalculator(Line line, DistanceFare distanceFare, AgePolicy agePolicy) {
        this.line = line;
        this.distanceFare = distanceFare;
        this.agePolicy = agePolicy;
    }

    public int getCalculcate() {
        int fare = distanceFare.getFare(BASE_FARE + line.getFare());
        return agePolicy.getFare(fare);
    }
}
