package nextstep.subway.path.dto;

import nextstep.subway.auth.domain.Money;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.station.dto.StationPathResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private List<StationPathResponse> stationPathResponses;
    private double distance;

    private double charge;

    public PathResponse(List<StationPathResponse> stationPathResponses, double distance) {
        this.stationPathResponses = stationPathResponses;
        this.distance = distance;
    }

    protected PathResponse() {
    }

    public PathResponse(List<StationPathResponse> stationPathResponses, double distance, double charge) {
        this.stationPathResponses = stationPathResponses;
        this.distance = distance;
        this.charge = charge;
    }

    public static PathResponse from(PathResult pathResult, Money charge) {
        return new PathResponse(
                pathResult.getStations().stream()
                        .map(StationPathResponse::from)
                        .collect(Collectors.toList()),
                pathResult.getWeight(), charge.getAmount());
    }

    public List<StationPathResponse> getStationPathResponses() {
        return stationPathResponses;
    }

    public double getDistance() {
        return distance;
    }

    public List<String> stationNames() {
        return stationPathResponses.stream()
                .map(StationPathResponse::getName)
                .collect(Collectors.toList());
    }

    public double getCharge() {
        return charge;
    }
}
