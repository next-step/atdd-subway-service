package nextstep.subway.line.dto;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class PathResponse {
    private List<Station> stations;
    private int distance;

    protected PathResponse() {

    }

    public PathResponse(List<Station> stations, int distance) {
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
