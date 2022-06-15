package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int fare;

    public PathResponse() {
    }

    public PathResponse(Path path, Fare fare) {
        stations = path.getShortestPath().stream().
                map(station -> StationResponse.of(station)).
                collect(Collectors.toList());
        distance = path.getShortestDistance();
        this.fare = fare.getTotalFare();
    }

    public List<StationResponse> getStations() {
        return this.stations;
    }

    public int getDistance() {
        return this.distance;
    }

    public int getFare() {
        return fare;
    }
}
