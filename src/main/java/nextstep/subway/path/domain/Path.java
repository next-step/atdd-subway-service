package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.station.domain.Station;

public final class Path {

    private final List<Station> stations;
    private final Integer totalDistance;
    private final Integer totalFare;

    private Path(List<Station> stations, Integer totalDistance) {
        this.stations = stations;
        this.totalDistance = totalDistance;
        this.totalFare = FarePolicy.calculateOverFare(totalDistance);
    }

    public static Path of(List<Station> stations, Integer totalDistance) {
        return new Path(stations, totalDistance);
    }

    public List<Station> getStations() {
        return stations;
    }

    public Integer getTotalDistance() {
        return totalDistance;
    }

    public Integer getTotalFare() {
        return totalFare;
    }
}
