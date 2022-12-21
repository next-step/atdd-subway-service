package nextstep.subway.path.dto;

import nextstep.subway.fare.domain.DistanceFarePolicy;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private final List<StationResponse> stations;
    private final int distance;
    private final int fare;

    private PathResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse from(List<Station> stations, int distance) {

        return new PathResponse(stationsResponse(stations), distance, DistanceFarePolicy.calculate(distance).value());
    }

    private static List<StationResponse> stationsResponse(List<Station> stations) {
        return stations.stream()
                .map(eachStation -> StationResponse.of(eachStation))
                .collect(Collectors.toList());
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}