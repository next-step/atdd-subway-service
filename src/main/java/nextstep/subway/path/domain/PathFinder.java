package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.graph.DefaultWeightedEdge;

public class PathFinder {

    private ShortestPathAlgorithm algorithm;
    private WeightedGraph<Station, DefaultWeightedEdge> graph;

    public PathFinder() {
    }

    public PathFinder(ShortestPathAlgorithm algorithm, WeightedGraph<Station, DefaultWeightedEdge> graph) {
        this.algorithm = algorithm;
        this.graph = graph;
    }

    public Path getDijkstraShortestPath(Station startStation, Station endStation) {
        return new Path();
    }
}
