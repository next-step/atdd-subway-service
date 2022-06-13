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
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.http.HttpStatus;

public class PathFinder {
    private WeightedMultigraph<Long, SectionEdge> graph;

    public PathFinder(List<Line> lines) {
        graph = new WeightedMultigraph<>(SectionEdge.class);
        lines.forEach(this::registerPath);
    }

    private void registerPath(Line line) {
        registerEdges(line.getStations());
        registerEdgeWith(line.getSections());
    }

    private void registerEdges(List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station.getId());
        }
    }

    public void registerEdgeWith(List<Section> sections) {
        for (Section section : sections) {
            SectionEdge edge = graph.addEdge(section.getUpStationId(), section.getDownStationId());
            edge.addSection(section);
            graph.setEdgeWeight(edge, section.getDistanceValue());
        }
    }

    public Path findPath(Station source, Station target) {
        validateEdge(source, target);
        DijkstraShortestPath<Long, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        try {
            GraphPath<Long, SectionEdge> graphPath = dijkstraShortestPath.getPath(source.getId(), target.getId());
            validatePath(graphPath);
            return Path.of(graphPath.getVertexList(), (int) graphPath.getWeight(), graphPath.getEdgeList());
        } catch (IllegalArgumentException e) {
            throw new CannotFindPathException(ExceptionType.NOT_FOUND_STATION);
        }
    }

    private void validateEdge(Station source, Station target) {
        if (source.equals(target)) {
            throw new BadRequestException(ExceptionType.CAN_NOT_SAME_STATION);
        }
    }

    private void validatePath(GraphPath<Long, SectionEdge> graphPath) {
        if (graphPath == null) {
            throw new CannotFindPathException(HttpStatus.UNPROCESSABLE_ENTITY, ExceptionType.IS_NOT_CONNECTED_STATION);
        }
    }
}
