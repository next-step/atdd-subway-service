package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.Objects;

public class StationPath {
    private final List<Station> stations;
    private final int distance;
    private final int extraCharge;

    public StationPath(final List<Station> stations, final int distance, final int extraCharge) {
        this.stations = stations;
        this.distance = distance;
        this.extraCharge = extraCharge;
    }

    public static StationPath of(final List<Station> stations, final double distance, final int extraCharge) {
        return new StationPath(stations, (int) distance, extraCharge);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "ShortestPathResponse{" +
                "stations=" + stations +
                ", distance=" + distance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StationPath that = (StationPath) o;
        return distance == that.distance && Objects.equals(stations, that.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance);
    }
}
