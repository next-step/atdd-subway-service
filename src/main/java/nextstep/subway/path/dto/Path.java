package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Stations;

public class Path {

    private Stations stations;
    private Distance distance;

    public Path(Stations stations, Distance distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public Stations getStations() {
        return stations;
    }

    public Distance getDistance() {
        return distance;
    }
}
