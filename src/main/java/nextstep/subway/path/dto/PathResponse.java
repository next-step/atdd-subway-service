package nextstep.subway.path.dto;

import java.util.List;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

    private final int distance;
    private final List<StationResponse> stations;
    private final int fare;

    private PathResponse(int distance, List<StationResponse> stations, int fare) {
        this.distance = distance;
        this.stations = stations;
        this.fare = fare;
    }

    public static PathResponse of(int distance, List<Station> stations, int fare) {
        return new PathResponse(distance, StationResponse.ofList(stations), fare);
    }

    public static PathResponse of(Path path) {
        return new PathResponse(path.getTotalDistance(),
            StationResponse.ofList(path.getStations()),
            path.getTotalFare());
    }

    public int getDistance() {
        return distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getFare() {
        return fare;
    }
}
