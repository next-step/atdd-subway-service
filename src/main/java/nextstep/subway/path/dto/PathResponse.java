package nextstep.subway.path.dto;

import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private double weight;

    public PathResponse(GraphPath<Station, DefaultWeightedEdge> shortestPath) {
        this.stations = StationResponse.listOf(shortestPath.getVertexList());
        this.weight = shortestPath.getWeight();
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public double getWeight() {
        return weight;
    }
}
