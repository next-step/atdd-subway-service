package nextstep.subway.path.dto;

import java.util.List;
import nextstep.subway.station.domain.Station;

public class Path {

    private List<Station> stations;
    private int distance;

    private Path(List<Station> stations, int distance) {
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
