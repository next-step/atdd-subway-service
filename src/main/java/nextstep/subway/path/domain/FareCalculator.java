package nextstep.subway.path.domain;

public class FareCalculator {
    private final Path path;

    public FareCalculator(Path path) {
        this.path = path;
    }

    public int calculate() {
        return DistanceFarePolicy.calculate(path.getDistance(), path.getExtraFare());
    }
}
