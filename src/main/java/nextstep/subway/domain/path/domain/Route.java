package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.line.domain.Distance;
import nextstep.subway.domain.station.domain.Station;

import java.util.List;

public class Route {

    private List<Station> stations;
    private Distance distance;

    public Route(final List<Station> stations, final Distance distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance.getDistance();
    }
}
