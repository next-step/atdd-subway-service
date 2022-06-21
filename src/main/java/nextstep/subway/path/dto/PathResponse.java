package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private final List<StationResponse> stations;
    private final Integer distance;
    private final Integer fare;

    public PathResponse(List<StationResponse> stations, Integer distance, Integer fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(List<Station> stations, Distance distance, Fare fare) {
        return new PathResponse(StationResponse.of(stations), distance.getDistance(), fare.getFare());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public Integer getFare() {
        return fare;
    }
}
