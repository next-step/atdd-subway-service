package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

public class PathFinder {

    private ShortestPathAlgorithm<Station, SectionEdge> algorithm;
    private WeightedGraph<Station, DefaultWeightedEdge> graph;

    public PathFinder() {
    }

    public PathFinder(ShortestPathAlgorithm algorithm, WeightedGraph<Station, DefaultWeightedEdge> graph) {
        this.algorithm = algorithm;
        this.graph = graph;
    }

    public Path getDijkstraShortestPath(Station startStation, Station endStation) {
        List<Station> path = algorithm.getPath(startStation, endStation).getVertexList();
        int totalDistance = (int) algorithm.getPathWeight(startStation, endStation);
        return new Path(path, totalDistance);
    }
}
