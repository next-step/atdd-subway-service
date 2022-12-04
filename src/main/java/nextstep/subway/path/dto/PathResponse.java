package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;

    private PathResponse(List<Station> stations, int distance){
        this.stations = stations.stream().map(StationResponse::of).collect(Collectors.toList());
        this.distance = distance;
    }

    public static PathResponse from(Path path) {
        return new PathResponse(path.getStations(), path.getDistance().value());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
