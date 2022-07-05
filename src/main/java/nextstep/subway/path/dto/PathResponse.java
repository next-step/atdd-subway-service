package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stationResponses;
    private double distance;
    private int fare;

    public PathResponse() {
    }

    public PathResponse(List<Station> stations, double distance) {
        this.stationResponses = stations.stream().map(StationResponse::of).collect(Collectors.toList());
        this.distance = distance;
        this.fare = 0;
    }

    public PathResponse(List<Station> stations, double distance, int fare) {
        this.stationResponses = stations.stream().map(StationResponse::of).collect(Collectors.toList());
        this.distance = distance;
        this.fare = fare;
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }

    public List<Station> toStations() {
        return stationResponses.stream().map(Station::of).collect(Collectors.toList());
    }

    public double getDistance() {
        return distance;
    }

    public long getFare() {
        return fare;
    }
}
