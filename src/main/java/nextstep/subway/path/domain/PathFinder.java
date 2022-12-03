package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

public class PathFinder {

    private final List<Station> stations;
    private final double weight;

    public PathFinder(List<Station> stations, double weight) {
        this.stations = stations;
        this.weight = weight;
    }

    public static PathFinder of(PathStrategy strategy, Station sourceStation, Station targetStation) {
        return strategy.getShortPath(sourceStation, targetStation);
    }

    public static PathFinder from(GraphPath<Station, DefaultWeightedEdge> graph) {
        return new PathFinder(graph.getVertexList(), graph.getWeight());
    }

    public List<Station> getStations() {
        return stations;
    }

    public double getWeight() {
        return weight;
    }
}
