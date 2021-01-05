package nextstep.subway.path.dto;

import lombok.NoArgsConstructor;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

import static java.util.stream.Collectors.toList;

@NoArgsConstructor
public class PathFinderResponse {
    private List<StationResponse> stations;
    private double distance;

    private PathFinderResponse(final GraphPath<Station, DefaultWeightedEdge> shortestPath) {
        distance = shortestPath.getWeight();
        stations = shortestPath.getVertexList()
            .stream()
            .map(StationResponse::of)
            .collect(toList());
    }

    public static PathFinderResponse of(final GraphPath<Station, DefaultWeightedEdge> shortestPath) {
        return new PathFinderResponse(shortestPath);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public double getDistance() {
        return distance;
    }
}
