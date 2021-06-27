package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

public class Paths {

    private final List<Station> shortestStationRoutes;
    private final int totalDistance;

    private Paths(final GraphPath<Station, DefaultWeightedEdge> graphPath) {
        this.shortestStationRoutes = graphPath.getVertexList();
        this.totalDistance = Double.valueOf(graphPath.getWeight()).intValue();
    }

    public static Paths of(final GraphPath<Station, DefaultWeightedEdge> graphPath) {
        return new Paths(graphPath);
    }

    public List<Station> getShortestStationRoutes() {
        return this.shortestStationRoutes;
    }

    public int getTotalDistance() {
        return this.totalDistance;
    }
}
