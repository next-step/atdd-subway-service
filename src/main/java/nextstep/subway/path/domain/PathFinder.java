package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.ui.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;
import java.util.stream.Collectors;

public class PathFinder {
    private final SubwayGraph graph;
    private final List<Section> sections;
    public static final int BASIC_FARE = 1250;
    public static final int ADDITIONAL_FARE_DISTANCE_LEVEL1 = 10;
    public static final int ADDITIONAL_FARE_DISTANCE_LEVEL2 = 50;
    public static final int DISTANCE_UNIT_LEVEL1 = 5;
    public static final int DISTANCE_UNIT_LEVEL2 = 8;

    private PathFinder(List<Line> lines) {
        this.sections = lines.stream()
                .flatMap(line -> line.getSections().stream())
                .collect(Collectors.toList());
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

        List<Station> stations = shortestPath.getVertexList();

        int additionalFareOfLine = checkIsAdditionalFareOfLine(stations);
        int fare = BASIC_FARE + additionalFareOfLine + calculateAdditionalFareOfDistance(shortestPath.getWeight());
        return new PathResponse(responses, (int) shortestPath.getWeight(), fare);
    }

    private int checkIsAdditionalFareOfLine(final List<Station> stations) {
        return sections.stream()
                .filter(section -> stations.containsAll(section.stations()))
                .map(section -> section.getLine().getAdditionalFare())
                .max(Integer::compareTo)
                .orElse(0);
    }

    private int calculateAdditionalFareOfDistance(final double weight) {
        if (weight > ADDITIONAL_FARE_DISTANCE_LEVEL2) {
            return calculateOverFare(weight - ADDITIONAL_FARE_DISTANCE_LEVEL2, DISTANCE_UNIT_LEVEL2);
        }

        if (weight > ADDITIONAL_FARE_DISTANCE_LEVEL1) {
            return calculateOverFare(weight - ADDITIONAL_FARE_DISTANCE_LEVEL1, DISTANCE_UNIT_LEVEL1);
        }

        return 0;
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
