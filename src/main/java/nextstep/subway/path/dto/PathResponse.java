package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private List<StationResponse> stations;
    private Integer distance;

    private PathResponse() {
    }

    public PathResponse(List<Station> stations, Distance distance) {
        this(stations.stream()
                        .map(StationResponse::of)
                        .collect(Collectors.toList()),
                distance.toInt());
    }

    public PathResponse(List<StationResponse> stations, Integer distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public PathResponse(int distance, String ...stationsName) {
        this.distance = distance;
        stations = Arrays.stream(stationsName)
                .map(name -> new StationResponse(0L, name, null, null))
                .collect(Collectors.toList());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }
}
