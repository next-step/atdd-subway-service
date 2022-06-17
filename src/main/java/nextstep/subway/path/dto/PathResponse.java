package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private List<StationResponse> stations;
    private double distance;

    public PathResponse() {
    }

    public PathResponse(Station station, double distance) {
        this.stations = Collections.singletonList(StationResponse.of(station));
        this.distance = distance;
    }

    public PathResponse(List<Station> stations, double distance) {
        this.stations = stations.stream().map(StationResponse::of).collect(Collectors.toList());
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations.stream().map(Station::of).collect(Collectors.toList());
    }

    public double getDistance() {
        return distance;
    }
}
