package nextstep.subway.path.domain;

import nextstep.subway.exception.InvalidPathSearchingException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionEdge;
import nextstep.subway.line.domain.SectionMultigraph;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class PathFinderUsingWeightedMultigraph implements PathFinder{

    private SectionMultigraph<Station, SectionEdge> weightedMultigraph;

    public PathFinderUsingWeightedMultigraph(List<Line> lines) {
        makeGraph(lines);
    }

    public void makeGraph(List<Line> lines) {
        weightedMultigraph = new SectionMultigraph(SectionEdge.class);
        for (Line line : lines) {
            addVerticesAndEdgesOf(line);
        }
    }

    public void addVerticesAndEdgesOf(Line line) {
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
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath
                = new DijkstraShortestPath(weightedMultigraph);
        GraphPath<Station, DefaultWeightedEdge> path
                = dijkstraShortestPath.getPath(sourceStation, targetStation);

        return new ShortestPath(path);
    }

    public List<ShortestPath> executeKShortest(Station sourceStation, Station targetStation) {
        validateSourceTarget(sourceStation, targetStation);
        KShortestPaths<Station, DefaultWeightedEdge> kShortestPaths
                = new KShortestPaths(weightedMultigraph, 5);
        List<GraphPath<Station, DefaultWeightedEdge>> paths
                = kShortestPaths.getPaths(sourceStation, targetStation);

        return paths.stream()
                .map(ShortestPath::new)
                .collect(Collectors.toList());
    }

    public void validateSourceTarget(Station source, Station target) {
        if (source.equals(target)) {
            throw new InvalidPathSearchingException("경로를 검색하려는 출발역과 도착역이 같습니다.");
        }
    }
}
