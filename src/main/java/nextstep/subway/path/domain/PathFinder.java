package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.Path;
import nextstep.subway.path.exception.IllegalFindingPathException;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

@Component
public class PathFinder {

    public Path findShortestPath(Sections sections, Station sourceStation, Station targetStation) {
        validateSameSourceTarget(sourceStation, targetStation);
        validateNonExistentSourceTarget(sections, sourceStation, targetStation);

        PathGraph pathGraph = createPathGraph(sections);
        return pathGraph.findShortestPath(sourceStation, targetStation);
    }

    private PathGraph createPathGraph(Sections sections) {
        return new PathGraph(sections);
    }

    private void validateSameSourceTarget(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalFindingPathException("출발지와 도착지가 같을 수 없습니다.");
        }
    }

    private void validateNonExistentSourceTarget(Sections sections, Station source, Station target) {
        List<Station> stationsForValidating = Stream.of(source, target)
                .filter(station -> !sections.isExistingStation(station))
                .collect(Collectors.toList());

        if (!stationsForValidating.isEmpty()) {
            throw new IllegalFindingPathException(format("해당 역이 존재 하지 않아 경로 탐색이 불가합니다. / %s", stationsForValidating));
        }
    }
}
