package nextstep.subway.line.dto;

import nextstep.subway.path.dto.StationPathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {

    private List<StationPathResponse> stations;

    public PathResponse(List<StationPathResponse> stations) {
        this.stations = stations;
    }

    public static PathResponse from(List<Station> stationsInPath) {
        return null;
    }

    public List<StationPathResponse> getStations() {
        return this.stations;
    }
}
