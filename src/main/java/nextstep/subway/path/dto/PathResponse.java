package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;

    public PathResponse(List<Station> stations, int distance) {
        this.stations = mapToStationResponse(stations);
        this.distance = distance;
    }

    private List<StationResponse> mapToStationResponse(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::of)
                .collect(toList());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
