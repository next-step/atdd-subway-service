package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class ShortestPathResponse {

    private List<Station> stations;
    private int distance;

    public ShortestPathResponse() {
    }

    public ShortestPathResponse(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static ShortestPathResponse of(List<Station> stations, int shortestDistance) {
        return new ShortestPathResponse(stations, shortestDistance);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
