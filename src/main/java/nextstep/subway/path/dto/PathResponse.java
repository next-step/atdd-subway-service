package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private final List<StationResponse> stations;
    private final int distance;

    private PathResponse(List<Station> stations, Distance distance) {
        this.stations = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        this.distance = distance.value();
    }

    public static PathResponse from(Path path) {
        return new PathResponse(path.getStations(), path.getDistance());
    }

    public List<StationResponse> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public int getDistance() {
        return distance;
    }
}
