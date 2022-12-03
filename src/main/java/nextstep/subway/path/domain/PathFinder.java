package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.ui.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PathFinder {
    private final SubwayGraph graph;
    public static final int BASIC_FARE = 1250;
    public static final int ADDITIONAL_FARE_DISTANCE_LEVEL1 = 10;
    public static final int ADDITIONAL_FARE_DISTANCE_LEVEL2 = 50;
    public static final int DISTANCE_UNIT_LEVEL1 = 5;
    public static final int DISTANCE_UNIT_LEVEL2 = 8;

    private PathFinder(List<Line> lines) {
        this.graph = new SubwayGraph(DefaultWeightedEdge.class, lines);
    }

    public static PathFinder from(final List<Line> lines) {
        return new PathFinder(lines);
    }

    public PathResponse getShortestPath(final Station sourceStation, final Station targetStation) {
        validate(sourceStation, targetStation);
        GraphPath<Station, DefaultWeightedEdge> shortestPath =
                new DijkstraShortestPath<>(graph).getPath(sourceStation, targetStation);
        validate(shortestPath);
        List<StationResponse> responses = shortestPath.getVertexList()
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        int additionalFare = calculateAdditionalFare(shortestPath.getWeight());
        return new PathResponse(responses, (int) shortestPath.getWeight(), additionalFare);
    }

    private int calculateAdditionalFare(final double weight) {
        if (weight > ADDITIONAL_FARE_DISTANCE_LEVEL2) {
            return BASIC_FARE + calculateOverFare(weight - ADDITIONAL_FARE_DISTANCE_LEVEL2, DISTANCE_UNIT_LEVEL2);
        }

        if (weight > ADDITIONAL_FARE_DISTANCE_LEVEL1) {
            return BASIC_FARE + calculateOverFare(weight - ADDITIONAL_FARE_DISTANCE_LEVEL1, DISTANCE_UNIT_LEVEL1);
        }

        return BASIC_FARE;
    }

    private int calculateOverFare(double distance, int distanceUnit) {
        return (int) ((Math.floor((distance - 1) / distanceUnit) + 1) * 100);
    }

    private void validate(final GraphPath<Station, DefaultWeightedEdge> shortestPath) {
        if (Objects.isNull(shortestPath)) {
            throw new IllegalArgumentException("출발역과 도착역이 연결이 되어 있지 않습니다.");
        }
    }

    private void validate(final Station sourceStation, final Station targetStation) {
        if (Objects.equals(sourceStation, targetStation)) {
            throw new IllegalArgumentException("출발역과 도착역이 " + sourceStation.getName() + "으로 동일합니다.");
        }
    }
}
