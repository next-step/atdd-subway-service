package nextstep.subway.path;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private final List<StationResponse> stations;
    private final long distance;

    public PathResponse(List<Station> list, long distance) {
        this.stations = list.stream().map(StationResponse::of).collect(Collectors.toList());
        this.distance = distance;
    }

    public long getDistance() {
        return distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
