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

    public static final int FIRST_BOUNDARY_DISTANCE = 10;
    public static final int SECOND_BOUNDARY_DISTANCE = 50;
    public static final int FIRST_ADDED_DISTANCE = 5;
    public static final int SECOND_ADDED_DISTANCE = 8;
    private final GraphPath<Station, DefaultWeightedEdge> path;
    private final int fare;

    public PathFinder(Station source, Station target, List<Line> lines) {
        validateEquals(source, target);

        int surcharge = lines.stream()
                .map(Line::getSurcharge)
                .max(Integer::compare)
                .orElse(0);

        fare = 1250 + surcharge;
        path = findShortest(source, target, lines);
    }

    private void validateEquals(Station source, Station target) {
        if (source.equals(target)) {
            throw new NotValidatePathException();
        }
    }

    public PathResponse findShortestPathToResponse() {
        List<Station> shortestStations = findShortestPath();
        int distance = calculateShortestDistance();

        if (distance <= FIRST_BOUNDARY_DISTANCE) {
            return new PathResponse(StationsResponse.of(shortestStations), distance, fare);
        }

        if (distance < SECOND_BOUNDARY_DISTANCE) {
            return new PathResponse(StationsResponse.of(shortestStations), distance, fare + calculateOverFare(distance, FIRST_ADDED_DISTANCE));
        }

        return new PathResponse(StationsResponse.of(shortestStations), distance, fare + calculateOverFare(distance, SECOND_ADDED_DISTANCE));
    }

    private int calculateOverFare(int distance, int overDistance) {
        return (int) ((Math.ceil(((distance - 10) - 1) / overDistance) + 1) * 100);
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
}
