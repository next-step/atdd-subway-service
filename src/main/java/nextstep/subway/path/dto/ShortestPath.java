package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class ShortestPath {
    private final List<Station> stations;
    private final long distance;

    public ShortestPath(List<Station> stations, long distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public long getDistance() {
        return distance;
    }

    public List<Station> getStations() {
        return stations;
    }
}
