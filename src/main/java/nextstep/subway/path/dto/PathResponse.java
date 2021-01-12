package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private Long distance;
    private Long fare;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, Long distance, Long fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Long getDistance() {
        return distance;
    }

    public Long getFare() {
        return fare;
    }

    public static PathResponse of(List<Station> stations, double distance, long fare) {
        List<StationResponse> stationResponsess = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new PathResponse(stationResponsess, (long) distance, fare);
    }
}
