package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.ShortestPath;

public class FareCalculator {
    private Lines lines;
    private ShortestPath shortestPath;
    private AgePolicy agePolicy;

    public FareCalculator(Lines lines, ShortestPath shortestPath, AgePolicy agePolicy) {
        this.lines = lines;
        this.shortestPath = shortestPath;
        this.agePolicy = agePolicy;
    }

    public int getCalculcate() {
        int lineFare = lines.getMaxFareByStations(shortestPath.getStations());
        int distanceFare = new DistanceFare(shortestPath.getDistance()).getFare();
        return agePolicy.getFare(lineFare + distanceFare);
    }
}

