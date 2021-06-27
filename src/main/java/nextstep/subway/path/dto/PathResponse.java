package nextstep.subway.path.dto;

import java.util.List;

import nextstep.subway.station.domain.Station;

public class PathResponse {

    private final List<Station> stations;
    private final int distance;

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
