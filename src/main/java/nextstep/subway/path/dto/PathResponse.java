package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;

    protected PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse from(Path path) {
        return new PathResponse(
                path.getStations()
                        .stream()
                        .map(it -> StationResponse.of(it))
                        .collect(Collectors.toList()),
                path.getDistance());
    }


    public List<StationResponse> getStations() {
        return new ArrayList<>(stations);
    }

    public int getDistance() {
        return distance;
    }

}
