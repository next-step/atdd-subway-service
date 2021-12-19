package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {
    private List<StationResponse> path;
    private double pathWeight;

    public PathResponse() {
    }

    private PathResponse(List<StationResponse> path, double pathWeight) {
        this.path = path;
        this.pathWeight = pathWeight;
    }

    public static PathResponse of(Path path) {
        List<StationResponse> stationResponsePath = path.getPath()
            .stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
        return new PathResponse(stationResponsePath, path.getPathWeight());
    }

    public List<StationResponse> getPath() {
        return path;
    }

    public double getPathWeight() {
        return pathWeight;
    }
}
