package nextstep.subway.path.dto;

import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int fare;

    protected PathResponse() {
    }

    public PathResponse(GraphPath<Station, DefaultWeightedEdge> shortestPath, int totalFee) {
        this.stations = StationResponse.listOf(shortestPath.getVertexList());
        this.distance = (int) shortestPath.getWeight();
        this.fare = totalFee;
    }

    public static PathResponse of(PathFinder pathFinder) {
        return new PathResponse(pathFinder.findShortestPath(), pathFinder.getTotalFee());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
