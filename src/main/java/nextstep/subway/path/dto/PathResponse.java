package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Distance;

import java.util.List;

public class PathResponse {
    private List<PathStation> stations;
    private int distance;

    public PathResponse(List<PathStation> stations, Distance distance) {
        this.stations = stations;
        this.distance = distance.get();
    }

    public static PathResponse of(List<PathStation> stations, Distance distance) {
        return new PathResponse(stations, distance);
    }

    public List<PathStation> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
