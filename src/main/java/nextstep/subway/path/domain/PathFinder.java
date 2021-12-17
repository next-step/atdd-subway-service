package nextstep.subway.path.domain;

import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.common.exception.PathSameException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PathFinder {
    private final PathStrategy pathStrategy = new DijkstraShortest();
    private final List<Line> lines;

    private PathFinder(final List<Line> lines) {
        this.lines = lines;
    }

    public static PathFinder from(final List<Line> lines) {
        return new PathFinder(lines);
    }

    public PathResponse findShortestPath(final Station source, final Station target) {
        validateSameStation(source, target);
        validateFoundBothStations(source, target);
        final Path path = pathStrategy.getShortestPath(lines, source, target);
        return PathResponse.from(path);
    }

    private void validateSameStation(final Station sourceStation, final Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new PathSameException();
        }
    }

    private void validateFoundBothStations(final Station sourceStation, final Station targetStation) {
        final List<Station> stations = lines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        if (!stations.contains(sourceStation) || !stations.contains(targetStation)) {
            throw new NotFoundException();
        }
    }
}
