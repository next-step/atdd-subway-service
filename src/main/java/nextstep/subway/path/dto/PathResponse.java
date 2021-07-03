package nextstep.subway.path.dto;

import nextstep.subway.path.domain.SubwayPath;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private final List<StationResponse> stations;
    private final int distance;

    public PathResponse(SubwayPath subwayPath) {
        this(StationResponse.ofList(subwayPath.stations()), subwayPath.distance());
    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
