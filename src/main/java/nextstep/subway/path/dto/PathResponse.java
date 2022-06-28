package nextstep.subway.path.dto;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int fare;

    private PathResponse() {}

    private PathResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(Path path, Fare fare) {
        List<StationResponse> stationResponses = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new PathResponse(stationResponses, path.getDistance(), fare.get());
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
