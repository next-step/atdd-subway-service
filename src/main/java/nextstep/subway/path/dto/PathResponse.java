package nextstep.subway.path.dto;

import java.util.List;
import nextstep.subway.station.domain.Station;

public class PathResponse {
    private List<Station> stations;

    private Long distance;

    public List<Station> getStations() {
        return stations;
    }

    public Long getDistance() {
        return distance;
    }

    public PathResponse(List<Station> stations, Long distance) {
        this.stations = stations;
        this.distance = distance;
    }
}
