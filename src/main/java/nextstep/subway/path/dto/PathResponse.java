package nextstep.subway.path.dto;

import nextstep.subway.path.domain.PathResult;
import nextstep.subway.station.dto.StationPathResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private List<StationPathResponse> stationPathResponses;
    private double distance;

    public PathResponse(List<StationPathResponse> stationPathResponses, double distance) {
        this.stationPathResponses = stationPathResponses;
        this.distance = distance;
    }

    public static PathResponse from(PathResult pathResult) {
        return new PathResponse(
                pathResult.getStations().stream()
                        .map(StationPathResponse::from)
                        .collect(Collectors.toList()),
                pathResult.getWeight());
    }

    public List<StationPathResponse> getStationPathResponses() {
        return stationPathResponses;
    }

    public double getDistance() {
        return distance;
    }

    public List<String> getStationNames() {
        return stationPathResponses.stream().map(StationPathResponse::getName).collect(Collectors.toList());
    }
}
