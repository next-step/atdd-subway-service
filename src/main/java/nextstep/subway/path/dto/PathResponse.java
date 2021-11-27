package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class PathResponse {

    private final List<Station> stations;
    private final int distance;

    public PathResponse(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public static PathResponse from(Path path) {
        return new PathResponse(path.getStations(), path.getDistance());
    }

    @Override
    public String toString() {
        return "PathResponse{" +
                "stations=" + stations +
                ", distance=" + distance +
                '}';
    }
}
