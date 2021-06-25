package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.path.domain.Path;

public class PathResponse {

    private List<VertexStationResponse> stations;
    private int distance;

    protected PathResponse() {
    }

    private PathResponse(List<VertexStationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(Path path) {
        List<VertexStationResponse> stationResponses = path.getStations()
                .stream()
                .map(VertexStationResponse::of)
                .collect(Collectors.toList());
        return new PathResponse(stationResponses, path.getTotalDistance());
    }

    public List<VertexStationResponse> getStations() {
        return this.stations;
    }

    public int getDistance() {
        return this.distance;
    }
}
