package nextstep.subway.path.dto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int fare;

    private PathResponse() {

    }

    public PathResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse from(Path path, Fare totalFare) {
        List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
        return new PathResponse(stations, path.getDistanceValue(), totalFare.value());
    }

    public List<StationResponse> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
