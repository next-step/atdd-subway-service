package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.path.domain.Path;

public class PathResponse {

    private List<VertexResponse> stations;
    private int distance;

    protected PathResponse() {
    }

    private PathResponse(List<VertexResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(Path path) {
        List<VertexResponse> vertexResponses = path.getPathVertexes()
                .stream()
                .map(VertexResponse::of)
                .collect(Collectors.toList());
        return new PathResponse(vertexResponses, path.getTotalDistance());
    }

    public List<VertexResponse> getStations() {
        return this.stations;
    }

    public int getDistance() {
        return this.distance;
    }
}
