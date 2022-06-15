package nextstep.subway.path.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class DijkstraPathFinder implements PathFinder {

    WeightedMultigraph<Station, SectionWeightedEdge> graph = new WeightedMultigraph(SectionWeightedEdge.class);

    public DijkstraPathFinder(List<Line> lines) {
        createStationGraphFromLines(lines);
    }

    private void createStationGraphFromLines(List<Line> lines) {

        HashSet<Section> allSections = new HashSet<>();
        for (Line line : lines) {
            allSections.addAll(line.getSections());
        }

        allSections.stream().forEach(section -> {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            SectionWeightedEdge sectionWeightedEdge = new SectionWeightedEdge(section);
            graph.addEdge(section.getUpStation(), section.getDownStation(), sectionWeightedEdge);
            graph.setEdgeWeight(sectionWeightedEdge, section.getDistance());
        });
    }

    @Override
    public Path findShortest(Station source, Station target) {
        validateSourceTargetEquality(source, target);
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath<Station, SectionWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
        validateNullPath(path);
        Set<Line> extraChargedLines = path.getEdgeList().stream().
                map(edge -> edge.getLine()).
                collect(Collectors.toSet());
        return new Path(path.getVertexList(), (int) path.getWeight(), extraChargedLines);
    }

    private void validateNullPath(GraphPath path) {
        if (path == null) {
            throw new IllegalArgumentException("경로를 검색 할 수 없습니다.");
        }
    }

    private void validateSourceTargetEquality(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역은 동일 할 수 없습니다.");
        }
    }
}
