package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private double distance;
    private double fare;

    public PathResponse() {
    }

    private PathResponse(List<StationResponse> stations, double distance, double fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(List<StationResponse> stationResponses, Path path, Fare fare) {
        return new PathResponse(stationResponses, path.getDistance(), fare.getFare());
    }

    public double getDistance() {
        return this.distance;
    }

    public List<StationResponse> getStations() {
        return this.stations;
    }

    public double getFare() {
        return this.fare;
    }
}
