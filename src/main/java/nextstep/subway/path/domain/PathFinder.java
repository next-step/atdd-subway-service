package nextstep.subway.path.domain;

import nextstep.subway.exception.NotFoundDataException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

import static nextstep.subway.exception.type.NotFoundDataExceptionType.NOT_FOUND_SOURCE_AND_TARGET_STATION;

public class PathFinder {

    private final List<Station> stations;
    private final int distance;

    public PathFinder(List<Station> stations, double weight) {
        validCheckStationIsEmpty(stations);
        this.stations = stations;
        this.distance = Math.toIntExact(Math.round(weight));
    }

    private void validCheckStationIsEmpty(List<Station> stations) {
        if (stations.isEmpty()) {
            throw new NotFoundDataException(NOT_FOUND_SOURCE_AND_TARGET_STATION.getMessage());
        }
    }

    public static PathFinder from(GraphPath<Station, DefaultWeightedEdge> graph) {
        return new PathFinder(graph.getVertexList(), graph.getWeight());
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
