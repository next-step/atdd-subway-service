package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

    List<StationResponse> stations;
    Double distance;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, Double distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(List<Station> stations, Double distance) {
        List<StationResponse> stationResponses = stations.stream().map(StationResponse::of).collect(Collectors.toList());
        return new PathResponse(stationResponses, distance);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Double getDistance() {
        return distance;
    }
}
