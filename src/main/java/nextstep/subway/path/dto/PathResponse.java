package nextstep.subway.path.dto;

import nextstep.subway.path.domain.SubwayShortestPath;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;

    protected PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(SubwayShortestPath path) {
        return new PathResponse(StationResponse.ofList(path.getStations()), path.getDistance());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
