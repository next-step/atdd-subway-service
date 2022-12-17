package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private List<Station> stations;
    private int distance;

    public Path(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return new ArrayList<>(this.stations);
    }

    public int getDistance() {
        return distance;
    }
}
