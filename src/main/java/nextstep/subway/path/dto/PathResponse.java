package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private final List<StationResponse> stations;
    private final Integer distance;
    private final Integer fee;

    public PathResponse(List<StationResponse> stations, Integer distance, Integer fee) {
        this.stations = stations;
        this.distance = distance;
        this.fee = fee;
    }

    public static PathResponse of(List<Station> stations, Integer distance) {
        // TODO fee 구현
        return new PathResponse(StationResponse.of(stations), distance, 0);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public Integer getFee() {
        return fee;
    }
}
