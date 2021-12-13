package nextstep.subway.path.domain;

import java.util.Comparator;
import java.util.List;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public final class Path {

    private final List<Station> stations;
    private final Integer totalDistance;
    private final Fare totalFare;

    private Path(List<Station> stations, Integer totalDistance, Fare totalFare) {
        this.stations = stations;
        this.totalDistance = totalDistance;
        this.totalFare = totalFare;
    }

    public static Path fromIncludedLines(List<Station> stations, Integer totalDistance, List<Line> includedLines) {
        return new Path(stations, totalDistance,
            Fare.valueOf(FarePolicy.calculateOverFare(totalDistance))
                .plus(getAdditionalFare(includedLines)));
    }

    public Path applyAgePolicy(AgeFarePolicy ageFarePolicy) {
        return new Path(stations, totalDistance, totalFare.getAgeFare(ageFarePolicy));
    }

    public List<Station> getStations() {
        return stations;
    }

    public Integer getTotalDistance() {
        return totalDistance;
    }

    public Integer getTotalFare() {
        return totalFare.get();
    }

    private static Fare getAdditionalFare(List<Line> includedLines) {
        return includedLines.stream()
            .map(it -> it.getAdditionalFare())
            .sorted(Comparator.comparingInt(Fare::get).reversed())
            .findFirst()
            .orElse(Fare.valueOf(0));
    }

}
