package nextstep.subway.path.domain;

import nextstep.subway.exception.NotValidatePathException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationsResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {

    public static final int MINIMUM_BOUNDARY_DISTANCE = 10;
    public static final int MAXIMUM_BOUNDARY_DISTANCE = 50;
    public static final int ADDED_DISTANCE_UNDER_MAXIMUM_BOUNDARY = 5;
    public static final int ADDED_DISTANCE_UPON_MAXIMUM_BOUNDARY = 8;
    public static final int OVER_FEE = 100;
    public static final int DEFAULT_FARE = 1250;
    public static final int MINIMUM_FARE = 0;
    public static final int MINIMUM_TEEAGER_AGE = 13;
    public static final int MAXIMUM_TEENAGER_AGE = 19;
    public static final int MINIMUM_CHILD_AGE = 6;
    public static final int MAXIMUM_CHILD_AGE = 13;
    public static final int ZERO_DISCOUNT = 0;
    public static final int TEENAGER_DISCOUNT_PERCENT = 20;
    public static final int CHILD_DISCOUNT_PERCENT = 50;
    public static final int PERCENTILE = 100;

    private final GraphPath<Station, DefaultWeightedEdge> path;
    private final int fare;

    public PathFinder(Station source, Station target, List<Line> lines) {
        validateEquals(source, target);

        fare = DEFAULT_FARE + maxSurcharge(lines);
        path = findShortest(source, target, lines);
    }

    private Integer maxSurcharge(List<Line> lines) {
        return lines.stream()
                .map(Line::getSurcharge)
                .max(Integer::compare)
                .orElse(MINIMUM_FARE);
    }

    private void validateEquals(Station source, Station target) {
        if (source.equals(target)) {
            throw new NotValidatePathException();
        }
    }

    public PathResponse findShortestPathToResponse(int age) {
        List<Station> shortestStations = findShortestPath();

        int distance = calculateShortestDistance();

        int fare = calculateFare(distance);

        int discount = discountFareByAge(age, fare);

        return new PathResponse(StationsResponse.of(shortestStations), distance, fare - discount);
    }

    private int discountFareByAge(int age, int fare) {
        if (age >= MINIMUM_TEEAGER_AGE && age < MAXIMUM_TEENAGER_AGE) {
            return (TEENAGER_DISCOUNT_PERCENT * fare) / PERCENTILE;
        }

        if (age >= MINIMUM_CHILD_AGE && age < MAXIMUM_CHILD_AGE) {
            return (CHILD_DISCOUNT_PERCENT * fare) / PERCENTILE;
        }

        return ZERO_DISCOUNT;
    }

    private int calculateFare(int distance) {
        if (distance < MINIMUM_BOUNDARY_DISTANCE) {
            return fare;
        }

        if (distance < MAXIMUM_BOUNDARY_DISTANCE) {
            return fare + calculateOverFare(distance, ADDED_DISTANCE_UNDER_MAXIMUM_BOUNDARY);
        }

        return fare + calculateOverFare(distance, ADDED_DISTANCE_UPON_MAXIMUM_BOUNDARY);
    }

    private int calculateOverFare(int distance, int overDistance) {
        return (int) ((Math.ceil(((distance - MINIMUM_BOUNDARY_DISTANCE) - 1) / overDistance) + 1) * OVER_FEE);
    }

    private int calculateShortestDistance() {
        return (int) path.getWeight();
    }

    private List<Station> findShortestPath() {
        return path.getVertexList();
    }

    private GraphPath<Station, DefaultWeightedEdge> findShortest(Station source, Station target, List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        addAllLineSectionsToGraph(lines, graph);

        return createShortestPaths(source, target, graph);
    }

    private void addAllLineSectionsToGraph(List<Line> lines, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        lines.stream()
                .map(Line::getSections)
                .forEach(sections -> addSectionsToGraph(graph, sections));
    }

    private GraphPath<Station, DefaultWeightedEdge> createShortestPaths(
            Station source,
            Station target,
            WeightedMultigraph<Station, DefaultWeightedEdge> graph
    ) {
        GraphPath<Station, DefaultWeightedEdge> path = new DijkstraShortestPath<>(graph).getPath(source, target);

        if (path == null) {
            throw new NotValidatePathException();
        }

        return path;
    }

    private void addSectionsToGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Section> sections) {
        for (Section section : sections) {
            addSectionToGraph(graph, section);
        }
    }

    private void addSectionToGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();

        graph.addVertex(upStation);
        graph.addVertex(downStation);
        graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
    }

    public int getFare() {
        return fare;
    }
}
