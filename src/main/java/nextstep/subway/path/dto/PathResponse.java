package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private Long distance;

    public PathResponse(List<StationResponse> stations, Long distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Long getDistance() {
        return distance;
    }

    public static PathResponse of(List<Station> stations, Long distance) {
        List<StationResponse> stationResponsess = stations.stream()
                .map(it -> StationResponse.of(it))
                .collect(Collectors.toList());
        return new PathResponse(stationResponsess, distance);
    }
}
