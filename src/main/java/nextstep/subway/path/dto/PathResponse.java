package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class PathResponse {

    private List<StationResponse> stations;

    private int distance;

    public PathResponse(List<Station> stations, Distance distance) {
        this.stations = stations.stream()
                .map(StationResponse::of)
                .collect(toList());
        this.distance = distance.getValue();
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
