package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.exception.BadRequestException;
import nextstep.subway.exception.CannotFindPathException;
import nextstep.subway.exception.ExceptionType;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.http.HttpStatus;

public class PathFinder {
    private static final WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

    public PathFinder(List<Line> lines) {
        lines.forEach(PathFinder::registerPath);
    }

    private static void registerPath(Line line) {
        registerEdges(line.getStations());
        registerEdgeWith(line.getSections());
    }

    private static void registerEdges(List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station.getId());
        }
    }

    public static void registerEdgeWith(List<Section> sections) {
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStationId(), section.getDownStationId()), section.getDistanceValue());
        }
    }

    public List<Long> findVertexList(Station source, Station target) {
        GraphPath<Long, DefaultWeightedEdge> graphPath = this.findPath(source, target);
        return graphPath.getVertexList();
    }

    public int getWeight(Station source, Station target) {
        GraphPath<Long, DefaultWeightedEdge> graphPath = this.findPath(source, target);
        return (int) graphPath.getWeight();
    }

    private GraphPath<Long, DefaultWeightedEdge> findPath(Station source, Station target) {
        validateEdge(source, target);
        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        try {
            GraphPath<Long, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(source.getId(), target.getId());
            validatePath(graphPath);
            return graphPath;
        } catch (IllegalArgumentException e) {
            throw new CannotFindPathException(ExceptionType.NOT_FOUND_STATION);
        }
    }

    private void validateEdge(Station source, Station target) {
        if (source.equals(target)) {
            throw new BadRequestException(ExceptionType.CAN_NOT_SAME_STATION);
        }
    }

    private void validatePath(GraphPath<Long, DefaultWeightedEdge> graphPath) {
        if (graphPath == null) {
            throw new CannotFindPathException(HttpStatus.UNPROCESSABLE_ENTITY, ExceptionType.IS_NOT_CONNECTED_STATION);
        }
    }
}
