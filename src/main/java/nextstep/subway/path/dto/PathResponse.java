package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;

    protected PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(List<Station> stations, double weight) {
        return new PathResponse(
                stations.stream()
                        .map(station -> StationResponse.of(station))
                        .collect(Collectors.toList()),
                (int) weight);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
