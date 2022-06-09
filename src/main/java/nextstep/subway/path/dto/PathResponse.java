package nextstep.subway.path.dto;

import java.util.List;
import nextstep.subway.fare.calculator.FareCalculator;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.StreamUtils;

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

    public static PathResponse from(Path path) {
        return new PathResponse(
                StreamUtils.mapToList(path.getStations(), StationResponse::of),
                path.getDistance(),
                FareCalculator.calculate(path.getDistance())
        );
    }

    public List<StationResponse> getStations() {
        return this.stations;
    }

    public int getDistance() {
        return this.distance;
    }

    public int getFare() { return this.fare; }
}
