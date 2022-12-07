package nextstep.subway.fare.domain;

public class FareCalculator {
    private int lineFare;
    private int distanceFare;
    private AgePolicy agePolicy;

    public FareCalculator(int lineFare, int distanceFare, AgePolicy agePolicy) {
        this.lineFare = lineFare;
        this.distanceFare = distanceFare;
        this.agePolicy = agePolicy;
    }

    public int getCalculcate() {
        return agePolicy.getFare(lineFare + distanceFare);
    }
}

