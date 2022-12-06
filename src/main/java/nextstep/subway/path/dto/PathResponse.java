package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    List<StationResponse> stations;
    int distance;

    public PathResponse() {

    }

    public PathResponse(List<Station> stations, Distance distance) {
        this.stations = stations.stream().map(StationResponse::of).collect(Collectors.toList());
        this.distance = distance.value();
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
