package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.ui.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PathFinder {
    private final SubwayGraph graph;
    private final List<Section> sections;

    private PathFinder(List<Line> lines) {
        this.sections = lines.stream()
                .flatMap(line -> line.getSections().stream())
                .collect(Collectors.toList());
        this.graph = new SubwayGraph(DefaultWeightedEdge.class, lines);
    }

    public static PathFinder from(final List<Line> lines) {
        return new PathFinder(lines);
    }

    public PathResponse getShortestPath(final Station sourceStation, final Station targetStation,
                                        final Consumer<Fare> discounter) {
        validate(sourceStation, targetStation);
        GraphPath<Station, DefaultWeightedEdge> shortestPath =
                new DijkstraShortestPath<>(graph).getPath(sourceStation, targetStation);
        validate(shortestPath);
        List<StationResponse> responses = shortestPath.getVertexList()
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        List<Station> stations = shortestPath.getVertexList();
        Fare fare = FareCalculator.calculateAdditionalFare(
                sections, stations, new Distance((int) shortestPath.getWeight())
        );

        discounter.accept(fare);

        return new PathResponse(responses, new Distance((int) shortestPath.getWeight()), fare);
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
