package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;

public class PathFareDistance {

    public static Fare of(final Path path) {
        Fare distanceFare = calculatorFare(path);
        return path.mergeFare(distanceFare);
    }

    private static Fare calculatorFare(final Path path) {
        return DistanceFarePolicy.from(path);
    }

    private PathFareDistance() {
        
    }
}
