package nextstep.subway.path.dto;

import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations) {
        this.stations = stations;
    }

    public static PathResponse of(List<StationResponse> stationResponses) {
        return new PathResponse(stationResponses);
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
