package nextstep.subway.path.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.path.domain.PathInfo;
import nextstep.subway.path.domain.StationEdge;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;

public class PathResponse {

    private List<StationResponse> stations = new ArrayList<>();

    private int distance;

    private double fare;

    public static PathResponse from(Stations pathStations) {
        return new PathResponse(pathStations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList()));
    }

    public PathResponse() {
    }

    private PathResponse(List<StationResponse> stations) {
        this.stations = stations;
    }

    public PathResponse(List<StationResponse> stations, int distance, double fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(GraphPath<Station, StationEdge> path, PathInfo pathInfo) {
        List<Station> pathStations = path.getVertexList();
        List<StationResponse> stations = pathStations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        return new PathResponse(stations, pathInfo.getPathDistanceValue(), pathInfo.getFareValue());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public double getFare() {
        return fare;
    }
}
