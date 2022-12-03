package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

public class PathFinder {

    private final List<Station> stations;
    private final double weight;

    private PathFinder(PathInterface strategy, Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> graph = strategy.getShortPath(source, target);
        graph.getWeight();
        graph.getVertexList();

        this.stations = graph.getVertexList();
        this.weight = graph.getWeight();
    }

    public static PathFinder of(PathInterface strategy, Station source, Station target) {
        return new PathFinder(strategy, source, target);
    }

    public List<Station> getStations() {
        return stations;
    }

    public double getWeight() {
        return weight;
    }
}
