package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.station.domain.Station;

public final class Path {

    private final List<Station> stations;
    private final Integer totalDistance;
    private final Fare totalFare;

    private Path(List<Station> stations, Integer totalDistance, Fare additionalFare) {
        this.stations = stations;
        this.totalDistance = totalDistance;
        this.totalFare = Fare.valueOf(FarePolicy.calculateOverFare(totalDistance)).plus(additionalFare);
    }

    public static Path of(List<Station> stations, Integer totalDistance, Fare additionalFare) {
        return new Path(stations, totalDistance, additionalFare);
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
}
