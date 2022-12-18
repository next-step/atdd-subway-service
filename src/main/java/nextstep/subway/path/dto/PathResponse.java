package nextstep.subway.path.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.station.domain.Stations;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

    private List<StationResponse> stations = new ArrayList<>();

    public static PathResponse from(Stations pathStations) {
        return new PathResponse(pathStations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList()));
    }

    public PathResponse() {
    }

    private PathResponse(List<StationResponse> stations) {
        this.stations = stations;
    }

    public void add(StationResponse station) {
        stations.add(station);
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
