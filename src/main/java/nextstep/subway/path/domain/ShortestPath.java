package nextstep.subway.path.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;

import nextstep.subway.line.domain.Fare;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class ShortestPath {
    private final List<Station> stations;
    private final int distance;
    private final int fare;

    private ShortestPath(List<Station> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static ShortestPath of(GraphPath<Station, FareWeightedEdge> graph) {
        return new ShortestPath(graph.getVertexList(), (int) graph.getWeight(), maxFare(graph.getEdgeList()));
    }

    private static int maxFare(List<FareWeightedEdge> edges) {
        return edges.stream().map(FareWeightedEdge::getFare)
                .map(Fare::toNumber)
                .max(Integer::compareTo).orElse(0);
    }

    public int calculateFareWithPolicy(int age) {
        int overFare = fare + DistanceFarePolicy.calculate(distance);
        AgeFarePolicy ageFarePolicy = AgeFarePolicy.find(age);
        return ageFarePolicy.calculate(overFare);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public List<StationResponse> toStationResponse() {
        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }
}
