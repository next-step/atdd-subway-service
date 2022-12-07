package nextstep.subway.path.dto;

import nextstep.subway.fare.domain.FareCalculator;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int fare;

    public PathResponse() {

    }

    public PathResponse(ShortestPath shortestPath, FareCalculator fareCalculator) {
        this.stations = shortestPath.getStations().stream().map(StationResponse::of).collect(Collectors.toList());
        this.distance = shortestPath.getDistance();
        this.fare = fareCalculator.getCalculcate();
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
