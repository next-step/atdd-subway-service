package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stationResponses;
    private int distance;
    private int fare;

    protected PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stationResponses = stations;
        this.distance = distance;
    }

    public PathResponse(Path path, Fare fare) {
        this.stationResponses = StationResponse.generateStationResponses(path.getStations());
        this.distance = path.getDistance();
        this.fare = fare.getValue();
    }

    public static PathResponse of(Path path, Fare fare) {
        return new PathResponse(path, fare);
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
