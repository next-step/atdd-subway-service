package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {
    private List<Station> stations;
    private int distance;

    public Path(List<Station> stations, int distance) {
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
}
