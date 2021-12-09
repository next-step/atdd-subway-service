package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private List<StationResponse> stations;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations) {
        this.stations = stations;
    }

    public static PathResponse ofList(List<Station> stations) {
        List<StationResponse> stationResponses = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new PathResponse(stationResponses);
    }

    public void setStations(List<StationResponse> stations) {
        this.stations = stations;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
