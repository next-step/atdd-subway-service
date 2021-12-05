package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Path {

    private final List<Station> stations;
    private final Distance distance;
    private final int maxAddedFare;

    public Path(List<Station> stations, int distance, int maxAddedFare) {
        this.stations = Collections.unmodifiableList(stations);
        this.distance = new Distance(distance);
        this.maxAddedFare = maxAddedFare;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance.getDistance();
    }

    public int getMaxAddedFare() {
        return maxAddedFare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path path = (Path) o;
        return Objects.equals(stations, path.stations) && Objects.equals(distance, path.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance);
    }
}
