package nextstep.subway.path.dto;

import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;

    private int fare;
    private PathResponse() {

    }
    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }
    public PathResponse(List<StationResponse> stations, Distance distance, Fare fare) {
        this.stations = stations;
        this.distance = distance.value();
        this.fare = fare.value();
    }

    public PathResponse(Path path) {
        this(StationResponse.of(path), path.getDistance(), path.getFare());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
