package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private long distance;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, long distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(Path path) {
        return new PathResponse(StationResponse.toStationResponses(path.getStations()), path.getDistance());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public long getDistance() {
        return distance;
    }
}
