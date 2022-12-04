package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.path.fare.Fare;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse2 {

    private List<StationResponse> stations;
    private Integer distance;
    private Integer fare;

    private PathResponse2() {
    }

    public PathResponse2(List<Station> stations, int distance, int fare) {
        this.stations = StationResponse.of(stations);
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse2 of(Path path, Fare fare) {
        return new PathResponse2(path.getStations(), path.getDistance().toInt(), fare.toInt());
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
