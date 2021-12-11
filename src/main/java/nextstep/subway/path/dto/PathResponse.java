package nextstep.subway.path.dto;

import java.util.List;

import nextstep.subway.station.domain.Station;

public class PathResponse {
    private List<Station> stations;
    private int distance;

    public PathResponse() {
    }

    private PathResponse(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(List<Station> stations, int distance) {
        return new PathResponse(stations, distance);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

}
