package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class PathFinder {
    private final ShortestPathCalculator shortestPathCalculator;

    public PathFinder(ShortestPathCalculator shortestPathCalculator) {
        this.shortestPathCalculator = shortestPathCalculator;
    }

    public PathResponse findPath(List<Line> lines, Station sourceStation, Station targetStation) {
        validateSourceEqualsTarget(sourceStation, targetStation);
        List<Section> sections = findSections(lines);

        return shortestPathCalculator.calculatePath(sections, sourceStation, targetStation);
    }

    private void validateSourceEqualsTarget(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
    }

    private List<Section> findSections(List<Line> lines) {
        List<Section> sections = new ArrayList<>();
        lines.stream()
                .map(line -> line.getSections())
                .forEach(sections::addAll);
        return sections;
    }
}
