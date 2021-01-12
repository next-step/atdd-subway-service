package nextstep.subway.path.dto;

import lombok.NoArgsConstructor;
import nextstep.subway.fare.Fare;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@NoArgsConstructor
public class PathFinderResponse {
    private List<StationResponse> stations;
    private double distance;
    private int fare;

    private PathFinderResponse(final GraphPath<Station, DefaultWeightedEdge> shortestPath, final int fare) {
        distance = shortestPath.getWeight();
        stations = shortestPath.getVertexList()
            .stream()
            .map(StationResponse::of)
            .collect(toList());
        this.fare = fare;
    }

    public static PathFinderResponse of(final GraphPath<Station, DefaultWeightedEdge> shortestPath, final int fare) {
        return new PathFinderResponse(shortestPath, fare);
    }

    public List<StationResponse> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public double getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
