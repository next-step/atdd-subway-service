package nextstep.subway.path.domain;

import java.util.List;
import java.util.Objects;
import nextstep.subway.common.exception.SubwayException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.Path;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class DijkstraShortestPathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

    private DijkstraShortestPathFinder(List<Line> lines) {
        lines.stream().forEach(line -> {
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
        sections.stream()
                .forEach(section -> graph.setEdgeWeight(addEdge(section), section.getDistance()));
    }

    private DefaultWeightedEdge addEdge(Section section) {
        return graph.addEdge(section.getUpStation(), section.getDownStation());
    }

    public Path findPath(Station source, Station target) {
        validateSameStations(source, target);

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(source, target);
        validateNotConnectStations(graphPath);

        List<Station> stations = graphPath.getVertexList();
        double weight = graphPath.getWeight();
        return Path.of(stations, (int) weight);
    }

    private void validateSameStations(Station source, Station target) {
        if (source.equals(target)) {
            throw new SubwayException("출발역과 도착역이 같습니다.");
        }
    }

    private void validateNotConnectStations(GraphPath<Station, DefaultWeightedEdge> graphPath) {
        if (Objects.isNull(graphPath)) {
            throw new SubwayException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
    }
}

