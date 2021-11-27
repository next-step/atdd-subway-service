package nextstep.subway.path.domain;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import java.util.List;


public class Path {

    private final Stations stations;
    private final int distance;


    public Path(Stations stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations.getStations();
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Path{" +
                "stations=" + stations +
                ", distance=" + distance +
                '}';
    }
}
