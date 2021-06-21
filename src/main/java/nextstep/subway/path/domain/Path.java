package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.Objects;

public class Path {

    private final List<Station> stations;
    private final int distance;

    private Path(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static Path of(List<Station> stations, int distance) {
        return new Path(stations, distance);
    }

    public List<Station> stations() {
        return stations;
    }

    public int distance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path path = (Path) o;
        return distance == path.distance && Objects.equals(stations, path.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance);
    }

    @Override
    public String toString() {
        return "Path{" +
                "stations=" + stations +
                ", distance=" + distance +
                '}';
    }
}
