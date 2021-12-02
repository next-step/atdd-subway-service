package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private final List<StationResponse> stations;
    private final int distance;
    private final int fee;


    public PathResponse(List<StationResponse> stations, int distance, int fee) {
        this.stations = stations;
        this.distance = distance;
        this.fee = fee;
    }

    public int getDistance() {
        return distance;
    }

    public static PathResponse from(Path path) {

        List<StationResponse> stations = path.getStations()
                                            .stream()
                                            .map(StationResponse::from)
                                            .collect(Collectors.toList());
        return new PathResponse(stations, path.getDistance(), path.getFee());
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

    public int getFee() {
        return fee;
    }
}
