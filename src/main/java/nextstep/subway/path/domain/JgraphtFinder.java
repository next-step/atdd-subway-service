package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathStation;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Optional;

public class JgraphtFinder implements PathFinder {
    private final WeightedMultigraph<PathStation, DefaultWeightedEdge> graph;
    private final DijkstraShortestPath<PathStation, DefaultWeightedEdge> dijkstraShortestPath;

    public JgraphtFinder(Sections sections) {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        addAllSections(sections.getSections());
    }

    public List<PathStation> findPath(PathStation source, PathStation target) {
        return createGraphPath(source, target).getVertexList();
    }

    public Distance findShortestDistance(PathStation source, PathStation target) {
        double weight = createGraphPath(source, target).getWeight();
        return new Distance((int) weight);
    }

    private GraphPath<PathStation, DefaultWeightedEdge> createGraphPath(PathStation source, PathStation target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
        return Optional.ofNullable(dijkstraShortestPath.getPath(source, target))
                .orElseThrow(() -> new IllegalArgumentException("출발역과 도착역이 연결이 되어 있지 않습니다."));
    }

    private void addAllSections(List<Section> sections) {
        sections.forEach(this::addVertexEdge);
    }

    private void addVertexEdge(Section section) {
        createVertex(section);
        createEdgeWeight(section);
    }

    private void createVertex(Section section) {
        graph.addVertex(PathStation.of(section.getUpStation()));
        graph.addVertex(PathStation.of(section.getDownStation()));
    }

    private void createEdgeWeight(Section section) {
        DefaultWeightedEdge edge = graph.addEdge(PathStation.of(section.getUpStation()), PathStation.of(section.getDownStation()));
        graph.setEdgeWeight(edge, section.getDistance());
    }
}
