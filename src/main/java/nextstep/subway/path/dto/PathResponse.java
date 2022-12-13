package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stationResponseList;
    private int distance;

    protected PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stationResponseList = stations;
        this.distance = distance;
    }

    public PathResponse(Path path) {
        this.stationResponseList = StationResponse.createStationResponse(path.getStations());
        this.distance = path.getDistance();
    }


    public static PathResponse fromPath(Path path) {
        return new PathResponse(path);
    }

    public List<StationResponse> getStationResponseList() {
        return stationResponseList;
    }

    public int getDistance() {
        return distance;
    }
}
