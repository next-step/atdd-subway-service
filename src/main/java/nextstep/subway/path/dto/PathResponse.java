package nextstep.subway.path.dto;

import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private final List<StationResponse> stations;
    private final int distance;
    private final int fee;

    private PathResponse(List<StationResponse> stations, int distance, int fee) {
        this.stations = stations;
        this.distance = distance;
        this.fee = fee;
    }

    public static PathResponse from(PathFinder pathFinder, int fee) {
        List<StationResponse> response = pathFinder
                .getStations()
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        return new PathResponse(response, pathFinder.getDistance(), fee);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFee() {
        return fee;
    }
}
