package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.util.Message;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;

public class Path {
    private final WeightedMultigraph<Station, PathEdge>
            graph = new WeightedMultigraph<>(PathEdge.class);
    private GraphPath<Station, PathEdge> path;

    public Path(List<Section> sections) {
        init(sections);
    }

    public static Path of(List<Section> sections) {
        return new Path(sections);
    }

    public void init(List<Section> sections) {
        addLineToGraph(sections);
    }

    private void addLineToGraph(List<Section> sections) {
        sections.forEach(section -> {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            PathEdge pathEdge = PathEdge.of(section);
            graph.addEdge(section.getUpStation(), section.getDownStation(), pathEdge);
        });
    }

    public ShortestPath selectShortestPath(Station source, Station target) {
        validateSection(source, target);
        try {
            path = new DijkstraShortestPath<>(graph).getPath(source, target);
            return ShortestPath.of(path.getVertexList(), path.getEdgeList(), (int) path.getWeight());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(Message.NOT_REGISTER_STATION);
        }
    }

    private void validateSection(Station source, Station target) {
        if (Objects.equals(source, target)) {
            throw new IllegalArgumentException(Message.EQUALS_START_STATION_AND_ARRIVAL_STATION);
        }
    }
}
