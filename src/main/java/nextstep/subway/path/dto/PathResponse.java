package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;

    public PathResponse(GraphPath<Station, DefaultWeightedEdge> shortestPath) {
        this.stations = mapToStationResponse(shortestPath.getVertexList());
        this.distance = (int) shortestPath.getWeight();
    }

    private List<StationResponse> mapToStationResponse(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::of)
                .collect(toList());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
