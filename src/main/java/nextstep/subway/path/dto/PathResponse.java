package nextstep.subway.path.dto;

import java.util.List;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.station.domain.Station;

public class PathResponse {
    private List<Station> stations;

    private Long distance;

    private Long fare;

    public List<Station> getStations() {
        return stations;
    }

    public Long getDistance() {
        return distance;
    }

    public Long getFare() {
        return fare;
    }

    public PathResponse(List<Station> stations, Long distance, Fare fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare.value();
    }
}
