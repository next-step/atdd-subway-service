package nextstep.subway.path.domain;

import nextstep.subway.exception.PathNotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.message.ExceptionMessage;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class DijkstraShortestPathFinder implements PathFinder {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

    private DijkstraShortestPathFinder(List<Line> lines) {
        lines.forEach(line -> {
            addVertex(line);
            setEdgeWeight(line.getSections());
        });
    }

    public static DijkstraShortestPathFinder from(List<Line> lines) {
        return new DijkstraShortestPathFinder(lines);
    }

    private void addVertex(Line line) {
        line.getStations()
                .forEach(graph::addVertex);
    }

    private void setEdgeWeight(List<Section> sections) {
        sections.forEach(section ->
                graph.setEdgeWeight(
                        graph.addEdge(section.getUpStation(), section.getDownStation()),
                        section.getDistance()
                )
        );
    }

    @Override
    public Path findShortestPath(Station source, Station target) {
        checkNotEqual(source, target);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
        checkPathExist(path);
        return Path.of(path.getVertexList(), (int) path.getWeight());
    }

    private void checkNotEqual(Station source, Station target) {
        if (source.equals(target)) {
            throw new PathNotFoundException(ExceptionMessage.SOURCE_AND_TARGET_EQUAL);
        }
    }

    private void checkPathExist(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new PathNotFoundException(ExceptionMessage.SOURCE_NOT_CONNECTED_TO_TARGET);
        }
    }
}
