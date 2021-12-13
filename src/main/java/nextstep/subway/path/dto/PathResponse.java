package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {

    private List<StationResponse> stations;

    private int distance;

    public PathResponse() {

    }

    public PathResponse(ShortestPath path) {
        this.stations = StationResponse.ofList(path.findPaths());
        this.distance = path.findWeight();
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
