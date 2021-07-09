package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {
    private List<Station> stations;
    private ShortestDistance distance;

    public Path(List<Station> stations, ShortestDistance distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public ShortestDistance getDistance() {
        return distance;
    }

    public Path changeDistance(ShortestDistance distance) {
        return new Path(stations, distance);
    }

    public Path changeStations(List stations) {
        return new Path(stations, distance);
    }
}
