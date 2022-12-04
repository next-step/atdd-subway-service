package nextstep.subway.path.dto;

import java.util.List;

import nextstep.subway.station.domain.Station;

public class PathResult {

    private List<Station> stations;

    private int distance;

    public PathResult(List<Station> stations, int distance) {
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
