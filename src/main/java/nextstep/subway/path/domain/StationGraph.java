package nextstep.subway.path.domain;

import nextstep.subway.exception.PathCannotFindException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

public class StationGraph {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> stationGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

    public StationGraph(List<Section> sections) {
        validate(sections);
        sections.forEach(section -> {
            addVertex(section.getUpStation());
            addVertex(section.getDownStation());
            addEdge(section);
        });
    }

    private void validate(List<Section> sections) {
        if (CollectionUtils.isEmpty(sections)) {
            throw new IllegalArgumentException("빈 구간 목록으로 그래프를 생성할 수 없습니다.");
        }
    }

    private void addVertex(Station station) {
        if (stationGraph.containsVertex(station)) {
            return;
        }
        stationGraph.addVertex(station);
    }

    private void addEdge(Section section) {
        stationGraph.setEdgeWeight(stationGraph.addEdge(section.getUpStation(),
                section.getDownStation()),
                section.getDistance().getDistance());
    }

    public boolean containsStation(Station station) {
        return stationGraph.containsVertex(station);
    }

    public boolean notContainsStation(Station station) {
        return !stationGraph.containsVertex(station);
    }

    public Path findShortestPath(Station source, Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(stationGraph);
        return convertToPath(dijkstra.getPath(source, target));
    }

    private Path convertToPath(GraphPath<Station, DefaultWeightedEdge> graphPath) {
        validateGraphPath(graphPath);
        return new Path(graphPath.getVertexList(), new Distance((int) graphPath.getWeight()));
    }

    private void validateGraphPath(GraphPath<Station, DefaultWeightedEdge> graphPath) {
        if (Objects.isNull(graphPath)) {
            throw new PathCannotFindException();
        }
    }
}
