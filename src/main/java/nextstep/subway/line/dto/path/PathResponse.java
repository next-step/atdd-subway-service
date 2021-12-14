package nextstep.subway.line.dto.path;

import java.util.List;
import nextstep.subway.line.domain.Money;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

    private final List<StationResponse> stations;
    private final Integer distance;
    private final Integer fare;

    public PathResponse(List<StationResponse> stations, Integer distance, Integer fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(List<StationResponse> stations, Integer distance) {
        return new PathResponse(stations, distance, 0);
    }

    public static PathResponse of(List<StationResponse> stations, Integer distance, Money fare) {
        return new PathResponse(stations, distance, fare.getMoney().intValue());
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
