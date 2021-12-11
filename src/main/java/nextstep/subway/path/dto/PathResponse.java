package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private double distance;

    public PathResponse() {
    }

    private PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(final List<Station> vertexList, final Double weight) {
        List<StationResponse> stations = vertexList.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new PathResponse(stations, weight.intValue());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public void setStations(final List<StationResponse> stations) {
        this.stations = stations;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
