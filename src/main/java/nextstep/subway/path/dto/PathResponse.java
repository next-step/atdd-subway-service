package nextstep.subway.path.dto;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;
    private int fare;

    public PathResponse() {
    }

    private PathResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

//    public static PathResponse of(Path path) {
//        return new PathResponse(StationResponse.ofList(path.getStations()), path.getDistance(), path.getFare());
//    }

    public static PathResponse of(Path path, Fare fare) {
        return new PathResponse(StationResponse.ofList(path.getStations()), path.getDistance(), fare.getFare());
    }

    public List<StationResponse> getStations() {
        return this.stations;
    }

    public int getDistance() {
        return this.distance;
    }

    public int getFare() {
        return this.fare;
    }
}
