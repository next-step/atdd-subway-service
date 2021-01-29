package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.util.Message;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;

public class Path {
    private final WeightedMultigraph<Station, DefaultWeightedEdge>
            graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private GraphPath<Station, DefaultWeightedEdge> path;

    public Path(List<Section> sections) {
        init(sections);
    }

    public static Path of(List<Section> sections) {
        return new Path(sections);
    }

    public void init(List<Section> sections) {
        addLineToGraph(graph, sections);
    }


    private void addLineToGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Section> sections) {
        sections.forEach(section -> {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            DefaultWeightedEdge edge = graph.addEdge(section.getUpStation(), section.getDownStation());
            graph.setEdgeWeight(edge, section.getDistance());
        });
    }

    public List<Station> selectShortestPath(Station source, Station target) {
        validateSection(source, target);
        try {
            path = new DijkstraShortestPath<>(graph).getPath(source, target);
            return path.getVertexList();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(Message.NOT_REGISTER_STATION);
        }
    }

    public int selectPathDistance() {
        return (int) path.getWeight();
    }

    private void validateSection(Station source, Station target) {
        if (Objects.equals(source, target)) {
            throw new IllegalArgumentException(Message.EQUALS_START_STATION_AND_ARRIVAL_STATION);
        }
    }
}
