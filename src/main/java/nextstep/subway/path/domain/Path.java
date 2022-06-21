package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.Objects;

public class Path {
    private List<Station> stations;
    private int distance;

    private Path (List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static Path of(List<Station> stations, int distance) {
        return new Path(stations, distance);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Path path = (Path) obj;
        return Objects.equals(stations, path.stations)
                && distance == path.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance);
    }
}
