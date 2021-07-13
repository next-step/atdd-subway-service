package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathsResponse {
    private List<StationResponse> stationList;

    private PathsResponse() {

    }

    private PathsResponse(List<StationResponse> stations) {
        stationList = stations;
    }

    public static PathsResponse of(List<StationResponse> shortestPath) {
        return new PathsResponse(shortestPath);
    }

    public List<StationResponse> getStationList() {
        return stationList;
    }
}
