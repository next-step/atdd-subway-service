package nextstep.subway.path.infra;

import nextstep.subway.path.domain.*;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Objects;

public class JgraphtPathFinder implements PathFinder {

    private JgraphtPathFinder() {
    }

    public static PathFinder getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public Path findShortest(final PathSections pathSections, final PathStation source, final PathStation target) {
        validate(source, target);

        WeightedMultigraph<PathStation, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        DijkstraShortestPath<PathStation, DefaultWeightedEdge> dijkstraShortestPath = initGraph(graph, pathSections);
        GraphPath<PathStation, DefaultWeightedEdge> shortestPath = dijkstraShortestPath.getPath(source, target);
        if (isDisconnectedPath(shortestPath)) {
            throw new IllegalArgumentException("연결되지 않은 지하철역 입니다.");
        }

        PathStations shortestPathStations = new PathStations(shortestPath.getVertexList());
        PathSections shortestPathSections = pathSections.findSections(shortestPathStations);
        double weight = shortestPath.getWeight();
        return new Path(shortestPathSections, (int) weight);
    }

    private void validate(final PathStation source, final PathStation target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역이 동일합니다.");
        }
    }

    private boolean isDisconnectedPath(final GraphPath<PathStation, DefaultWeightedEdge> path) {
        return Objects.isNull(path);
    }

    private DijkstraShortestPath<PathStation, DefaultWeightedEdge> initGraph(
            final WeightedMultigraph<PathStation, DefaultWeightedEdge> graph,
            final PathSections pathSections) {
        pathSections.forEach(graph::addVertex);

        pathSections.getPathSections()
                .forEach(pathSection -> graph.setEdgeWeight(graph.addEdge(
                        pathSection.getUpStation(),
                        pathSection.getDownStation()),
                        pathSection.getDistance())
                );
        return new DijkstraShortestPath<>(graph);
    }

    private static final class LazyHolder {

        private static final PathFinder INSTANCE = new JgraphtPathFinder();
    }
}
