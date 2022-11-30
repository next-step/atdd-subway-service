package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;

public class Path {

    private List<Station> stations;
    private Distance distance;

    public Path() {
    }

    public Path(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = new Distance(distance);
    }


    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance.getDistance();
    }
}
