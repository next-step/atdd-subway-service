package nextstep.subway.path.domain;

import nextstep.subway.constant.ErrorCode;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class PathFinder {
    private final WeightedMultigraph<Station, SectionEdge> graph;
    private final Lines lines;

    public PathFinder(List<Line> lines) {
        graph = new WeightedMultigraph<>(SectionEdge.class);
        this.lines = new Lines(lines);

        initializeVertex();
        initializeEdgeWeight();
    }

    private void initializeVertex() {
        lines.getStations()
                .forEach(graph::addVertex);
    }

    private void initializeEdgeWeight() {
        lines.getSections()
                .forEach(section -> {
                    SectionEdge sectionEdge = new SectionEdge(section);
                    graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, section.getDistance().get());
                });
    }

    public PathResponse shortestPath(Station sourceStation, Station targetStation) {
        validateBefore(sourceStation, targetStation);

        GraphPath<Station, SectionEdge> shortestPath =
                new DijkstraShortestPath<>(graph).getPath(sourceStation, targetStation);

        validateAfter(shortestPath);

        List<StationResponse> responses = shortestPath.getVertexList()
                .stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());

        // Todo 리팩토링 대상
        Lines pathLines = new Lines(shortestPath.getEdgeList()
                .stream()
                .map(SectionEdge::getLine)
                .collect(Collectors.toList()));

        int distance = (int) shortestPath.getWeight();
        return new PathResponse(responses, distance, new Fare(pathLines, distance));
    }

    private void validateBefore(Station sourceStation, Station targetStation) {
        validateSameStation(sourceStation, targetStation);
        validateNotExistLines(sourceStation, targetStation);
    }

    private void validateSameStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException(ErrorCode.FIND_PATH_SAME_SOURCE_TARGET.getMessage());
        }
    }

    private void validateNotExistLines(Station sourceStation, Station targetStation) {
        if (lines.hasNotStation(sourceStation) || lines.hasNotStation(targetStation)) {
            throw new IllegalArgumentException(ErrorCode.FIND_PATH_NOT_EXIST.getMessage());
        }
    }

    private void validateAfter(GraphPath<Station, SectionEdge> shortestPath) {
        if (shortestPath == null) {
            throw new IllegalArgumentException(ErrorCode.FIND_PATH_NOT_CONNECT.getMessage());
        }
    }
}
