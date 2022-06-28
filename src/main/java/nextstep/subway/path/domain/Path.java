package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Stations;
import nextstep.subway.station.domain.Station;

public class Path {
    private Stations stations;
    private Distance distance;

    public Path(List<Station> stations, int distance) {
        this.stations = Stations.of(stations);
        this.distance = Distance.of(distance);
    }

    public Stations getStations() {
        return stations;
    }

    public Distance getDistance() {
        return distance;
    }

}
