package nextstep.subway.path.dto;

import nextstep.subway.path.domain.FareCalculator;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private final List<StationResponse> stations;
    private final int distance;
    private final int fare;

    private PathResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(Path path) {
        List<StationResponse> stationResponses = path.getStations()
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        FareCalculator fareCalculator = new FareCalculator(path);
        return new PathResponse(stationResponses, path.getDistance(), fareCalculator.calculate());
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
