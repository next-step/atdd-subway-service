package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse ofList(Path path) {
        List<StationResponse> stationResponses = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new PathResponse(stationResponses, path.getDistance());
    }

    public void setStations(List<StationResponse> stations) {
        this.stations = stations;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
