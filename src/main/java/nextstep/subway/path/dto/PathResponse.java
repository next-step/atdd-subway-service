package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(GraphPath<Station, DefaultWeightedEdge> dijkstraPath) {
        List<StationResponse> stations = dijkstraPath.getVertexList().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new PathResponse(stations, (int) dijkstraPath.getWeight());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
