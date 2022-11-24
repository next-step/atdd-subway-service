package nextstep.subway.path.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;

    private PathResponse() {}

    public PathResponse(List<StationResponse> stations, Distance distance) {
        this.stations = new ArrayList<>(stations);
        this.distance = distance.value();
    }

    public static PathResponse from(Path path) {
        List<Station> stations = path.unmodifiableStations();
        return new PathResponse(stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList()),
                path.getDistance());
    }

    public List<StationResponse> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public int getDistance() {
        return distance;
    }
}
