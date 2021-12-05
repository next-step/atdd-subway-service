package nextstep.subway.line.dto;

import static java.util.stream.Collectors.toList;

import java.util.List;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

    private List<StationResponse> stations;
    private double distance;

    private PathResponse(List<StationResponse> stations, double distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse ofList(List<Station> stations, double distance) {
        List<StationResponse> collect = stations.stream()
            .map(StationResponse::of)
            .collect(toList());
        return new PathResponse(collect, distance);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public double getDistance() {
        return distance;
    }
}
