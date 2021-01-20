package nextstep.subway.path.dto;

import nextstep.subway.path.domain.FeeCalculator;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private Integer distance;
    private int fee;

    public PathResponse() {
    }

    public PathResponse(List<Station> stations, Integer distance) {
        this.stations = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        this.distance = distance;
        this.fee = FeeCalculator.calculateFee(distance);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }

    public int getFee() {
        return fee;
    }
}
