package nextstep.subway.path.domain;

import static java.util.Collections.*;

import java.util.List;

import nextstep.subway.station.domain.Station;

public class Path {

    private List<Station> stations;
    private int distance;

    Path(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public List<Station> getStations() {
        return unmodifiableList(stations);
    }
}
