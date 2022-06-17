package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private  List<StationResponse> stations;
    private  int distance;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public static PathResponse of(GraphPath<Station, DefaultWeightedEdge> path) {
        return new PathResponse(
                path.getVertexList().stream()
                        .map(StationResponse::of)
                        .collect(Collectors.toList()),
                Double.valueOf(path.getWeight()).intValue());
    }
}
