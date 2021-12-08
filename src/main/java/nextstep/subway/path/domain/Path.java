package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.station.domain.Station;

public final class Path {

    private final List<Station> stations;
    private final Integer totalDistance;

    private Path(List<Station> stations, Integer totalDistance) {
        this.stations = stations;
        this.totalDistance = totalDistance;
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
}
