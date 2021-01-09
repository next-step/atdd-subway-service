package nextstep.subway.fare.dto;

import nextstep.subway.path.dto.PathStation;

import java.util.List;

public class PathWithFareResponse {
    private final List<PathStation> stations;
    private final int distance;
    private final int fare;


    public PathWithFareResponse(List<PathStation> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<PathStation> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }

    public static PathWithFareResponse ofResponse(List<PathStation> stations, int distance, int fare) {
        return new PathWithFareResponse(stations, distance, fare);
    }
}
