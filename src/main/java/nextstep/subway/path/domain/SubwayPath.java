package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.List;

public class SubwayPath {
    private List<Station> stations;
    private int distance;

    public SubwayPath() {
    }

    public SubwayPath(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
