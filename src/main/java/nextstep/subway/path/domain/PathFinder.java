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

        List<StationResponse> responses = shortestPath.getVertexList()
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        return new PathResponse(responses, (int) shortestPath.getWeight());
    }

    private void validate(final Station sourceStation, final Station targetStation) {
        if(Objects.equals(sourceStation, targetStation)) {
            throw new IllegalArgumentException("출발역과 도착역이 " + sourceStation.getName() + "으로 동일합니다.");
        }
    }
}
