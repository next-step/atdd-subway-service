package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private long fare;

    private PathResponse() {
    }

    private PathResponse(List<StationResponse> stations, int distance, long fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse from(List<Station> stations, int distance, long fare) {
        return new PathResponse(
                stations.stream()
                    .map(eachStation -> StationResponse.of(eachStation))
                    .collect(Collectors.toList())
                , distance
                , fare);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
