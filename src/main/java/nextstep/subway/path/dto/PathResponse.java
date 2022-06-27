package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class PathResponse {

    private List<Station> stations;

    private int distance;

    public PathResponse(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
