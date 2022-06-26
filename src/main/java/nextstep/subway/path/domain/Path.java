package nextstep.subway.path.domain;

import java.util.List;
import java.util.Objects;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;

public class Path {
    private List<Station> stations;
    private Distance distance;

    public Path(List<Station> stations, Distance distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Distance getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Path{" + "stations=" + stations + ", distance=" + distance + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Path path = (Path) o;
        return Objects.equals(stations, path.stations) && Objects.equals(distance, path.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance);
    }
}
