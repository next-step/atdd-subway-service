package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class PathResponse {
    private int distance;
    private List<Station> stations;

    public PathResponse(int distance, List<Station> stations) {
        this.distance = distance;
        this.stations = stations;
    }

    public int getDistance() {
        return distance;
    }

    public List<Station> getStations() {
        return stations;
    }
}