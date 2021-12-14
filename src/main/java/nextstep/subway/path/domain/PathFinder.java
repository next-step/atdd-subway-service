package nextstep.subway.path.domain;

import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.common.exception.PathDisconnectedException;
import nextstep.subway.common.exception.PathSameException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PathFinder {
    private final Path path = new DijkstraShortestPath();
    private final List<Station> stations;

    private PathFinder(final List<Line> lines){
        creteGraph(lines);
        this.stations = lines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    public static PathFinder from(final List<Line> lines) {
        return new PathFinder(lines);
    }

    private void creteGraph(final List<Line> lines){
        path.createEdge(lines);
        path.createVertex(lines);
    }

    public PathResponse findShortestPath(final Station source, final Station target) {
        validateSameStation(source, target);
        validateFoundBothStations(source, target);
        final List<Station> stations = path.getVertexes(source, target);
        validateConnectedStations(stations);
        return PathResponse.of(stations, path.getWeight(source, target));
    }

    private void validateSameStation(final Station sourceStation, final Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new PathSameException();
        }
    }

    private void validateConnectedStations(final List<Station> stations) {
        if (CollectionUtils.isEmpty(stations)) {
            throw new PathDisconnectedException();
        }
    }

    private void validateFoundBothStations(final Station sourceStation, final Station targetStation) {
        if (!stations.contains(sourceStation) || !stations.contains(targetStation)) {
            throw new NotFoundException();
        }
    }
}
