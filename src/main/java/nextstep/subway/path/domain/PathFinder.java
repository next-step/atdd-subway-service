package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionEdge;
import nextstep.subway.line.domain.SectionMultigraph;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;

import java.util.List;
import java.util.stream.Collectors;

public class PathFinder {

    private SectionMultigraph<Station, SectionEdge> weightedMultigraph;

    public PathFinder(List<Line> lines) {
        makeWeightedMultigraph(lines);
    }

    private void makeWeightedMultigraph(List<Line> lines) {
        weightedMultigraph = new SectionMultigraph(SectionEdge.class);
        for (Line line : lines) {
            addVerticesAndEdgesOf(line);
        }
    }

    private void addVerticesAndEdgesOf(Line line) {
        for (Section section : line.getSections().get()) {
            weightedMultigraph.addVertex(section.getUpStation());
            weightedMultigraph.addVertex(section.getDownStation());
            weightedMultigraph.setEdgeAdditionalFare(
                    weightedMultigraph.addEdge(section.getUpStation(), section.getDownStation()),
                    section.getDistance(),
                    section.additionalFare());
        }
    }

    public ShortestPath executeDijkstra(Station sourceStation, Station targetStation) {
        validateSourceTarget(sourceStation, targetStation);
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath
                = new DijkstraShortestPath(weightedMultigraph);
        GraphPath<Station, SectionEdge> path
                = dijkstraShortestPath.getPath(sourceStation, targetStation);

        return new ShortestPath(path);
    }

    public List<ShortestPath> executeKShortest(Station sourceStation, Station targetStation) {
        validateSourceTarget(sourceStation, targetStation);
        KShortestPaths<Station, SectionEdge> kShortestPaths
                = new KShortestPaths(weightedMultigraph, 5);
        List<GraphPath<Station, SectionEdge>> paths
                = kShortestPaths.getPaths(sourceStation, targetStation);

        return paths.stream()
                .map(ShortestPath::new)
                .collect(Collectors.toList());
    }

    private void validateSourceTarget(Station source, Station target) {
        if (source.equals(target)) {
            throw new RuntimeException("경로를 검색하려는 출발역과 도착역이 같습니다.");
        }
    }
}
