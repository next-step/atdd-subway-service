package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class PathResponse {

    private final List<Station> stations;
    private final int distance;
    private final int fare;

    public PathResponse(List<Station> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(List<Station> stations, int distance, int fare) {
        return new PathResponse(stations, distance, fare);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
