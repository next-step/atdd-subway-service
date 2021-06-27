package nextstep.subway.path.domain;

import nextstep.subway.exception.CustomException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;
import java.util.Objects;

import static nextstep.subway.exception.CustomExceptionMessage.NOT_CONNECTED_SOURCE_AND_TARGET_STATION;
import static nextstep.subway.exception.CustomExceptionMessage.NOT_FOUND_PATHS;

public class Paths {

    private final List<Station> shortestStationRoutes;
    private final int totalDistance;

    private Paths(final GraphPath<Station, DefaultWeightedEdge> graphPath) {
        checkNotConnectedBetweenStations(graphPath);
        checkNotFoundPaths(graphPath.getWeight());
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

    private void checkNotConnectedBetweenStations(final GraphPath<Station, DefaultWeightedEdge> graphPath) {
        if (Objects.isNull(graphPath)) {
            throw new CustomException(NOT_CONNECTED_SOURCE_AND_TARGET_STATION);
        }
    }

    private void checkNotFoundPaths(final double weight) {
        if (Double.isInfinite(weight)) {
            throw new CustomException(NOT_FOUND_PATHS);
        }
    }
}
