package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    public int charge;
    private int distance;
    private List<StationResponse> stations;

    private PathResponse() {
    }

    public PathResponse(int charge, int distance, List<StationResponse> stations) {
        this.charge = charge;
        this.distance = distance;
        this.stations = stations;
    }

    public static PathResponse of(Path path) {
        List<StationResponse> stationsResponses = path.getStations().stream()
                .map(station -> StationResponse.of(station))
                .collect(Collectors.toList());
        return new PathResponse(1650, path.getDistance(), stationsResponses);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getCharge() {
        return this.charge;
    }

    public int getDistance() {
        return this.distance;
    }
}
