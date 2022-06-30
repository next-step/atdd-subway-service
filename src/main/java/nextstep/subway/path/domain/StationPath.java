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

    public int getExtraCharge() {
        return extraCharge;
    }

    @Override
    public String toString() {
        return "StationPath{" +
                "stations=" + stations +
                ", distance=" + distance +
                ", extraCharge=" + extraCharge +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final StationPath that = (StationPath) o;
        return distance == that.distance && extraCharge == that.extraCharge && Objects.equals(stations, that.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance, extraCharge);
    }
}
