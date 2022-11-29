package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.exception.PathException;
import nextstep.subway.path.exception.PathExceptionType;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class GraphPathFinder {

    private WeightedMultigraph<String, DefaultWeightedEdge> graph;

    public StationPath getShortestPath(List<Line> lines, Station start, Station destination) {
        validation(start, destination);
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertex(lines);
        addEdgeWeight(lines);

        return StationPath.of(new DijkstraShortestPath<>(graph).getPath(start.getName(), destination.getName()));
    }

    private void validation(final Station start, final Station destination) {
        if (start.equals(destination)) {
            throw new PathException(PathExceptionType.EQUALS_STATION);
        }
    }

    private void addVertex(final List<Line> lines) {
        for (Line line : lines) {
            line.getSortedStations()
                    .forEach(it -> graph.addVertex(it.getName()));
        }
    }

    private void addEdgeWeight(final List<Line> lines) {
        for (Line line : lines) {
            line.getSections()
                    .forEach(section -> {
                        DefaultWeightedEdge edge = graph.addEdge(section.getUpStation().getName(), section.getDownStation().getName());
                        graph.setEdgeWeight(edge, section.getDistance());
                    });
        }
    }
}
