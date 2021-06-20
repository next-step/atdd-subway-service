package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class PathFinder {

    private WeightedMultigraph weightedMultigraph;

    public PathFinder(List<Line> lines) {
        makeWeightedMultigraph(lines);
    }

    private void makeWeightedMultigraph(List<Line> lines) {
        weightedMultigraph = new WeightedMultigraph(DefaultWeightedEdge.class);
        for (Line line : lines) {
            List<Section> sections = line.getSections().get();
            for (Section section : sections) {
                weightedMultigraph.addVertex(section.getUpStation());
                weightedMultigraph.addVertex(section.getDownStation());
                weightedMultigraph.setEdgeWeight(weightedMultigraph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
            }
        }
    }

    public ShortestPath executeDijkstra(Station sourceStation, Station targetStation) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(weightedMultigraph);
        GraphPath path = dijkstraShortestPath.getPath(sourceStation, targetStation);

        return new ShortestPath(path);
    }

    public List<ShortestPath> executeKShortest(Station sourceStation, Station targetStation) {
        KShortestPaths kShortestPaths = new KShortestPaths(weightedMultigraph, 5);
        List<GraphPath> paths = kShortestPaths.getPaths(sourceStation, targetStation);

        return paths.stream()
                .map(graphPath -> new ShortestPath(graphPath))
                .collect(Collectors.toList());
    }
}
