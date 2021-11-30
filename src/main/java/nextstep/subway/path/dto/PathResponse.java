package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private final List<StationResponse> stations;
    private final int distance;


    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public static PathResponse from(Path path) {

        List<StationResponse> stations = path.getStations()
                                            .stream()
                                            .map(StationResponse::from)
                                            .collect(Collectors.toList());
        return new PathResponse(stations, path.getDistance());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    @Override
    public String toString() {
        return "PathResponse{" +
                "stations=" + stations +
                ", distance=" + distance +
                '}';
    }
}
