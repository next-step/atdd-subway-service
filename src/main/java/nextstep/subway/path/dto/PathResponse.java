package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {
    private List<StationResponse> path;
    private double pathWeight;
    private int fare;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> path, double pathWeight, int fare) {
        this.path = path;
        this.pathWeight = pathWeight;
        this.fare = fare;
    }

    public static PathResponse of(Path path, Fare fare) {
        List<StationResponse> stationResponsePath = path.getPath()
            .stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
        return new PathResponse(stationResponsePath, path.getDistance(), fare.getValue());
    }

    public List<StationResponse> getPath() {
        return path;
    }

    public double getPathWeight() {
        return pathWeight;
    }

    public int getFare() {
        return fare;
    }
}
