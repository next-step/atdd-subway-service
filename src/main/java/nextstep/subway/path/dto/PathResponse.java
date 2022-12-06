package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int fare;

    public PathResponse(List<StationResponse> stations, int distance, Fare fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare.value();
    }

    public static PathResponse from(Path path, Fare fare) {
        return new PathResponse(toStationResponses(path.stations()), path.distanceValue(), fare);
    }

    private static List<StationResponse> toStationResponses(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
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
