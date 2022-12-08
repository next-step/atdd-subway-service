package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.fare.Fare;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private List<StationResponse> stations;
    private Integer distance;
    private Integer fare;

    private PathResponse() {
    }

    public PathResponse(List<Station> stations, Distance distance) {
        this(stations.stream()
                        .map(StationResponse::of)
                        .collect(Collectors.toList()),
                distance.toInt());
    }

    public PathResponse(List<StationResponse> stations, Integer distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public PathResponse(List<Station> stations, Distance distance, Fare fare) {
        this.stations = StationResponse.of(stations);
        this.distance = distance.toInt();
        this.fare = fare.toInt();
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getFare() {
        return fare;
    }
}
