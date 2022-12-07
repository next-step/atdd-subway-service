package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Line;

public class FareCalculator {
    private Line line;
    private DistanceFare distanceFare;
    private AgePolicy agePolicy;

    public FareCalculator(Line line, DistanceFare distanceFare, AgePolicy agePolicy) {
        this.line = line;
        this.distanceFare = distanceFare;
        this.agePolicy = agePolicy;
    }

    public int getCalculcate() {
        int fare = distanceFare.getFare(line.getFare());
        return agePolicy.getFare(fare);
    }
}
