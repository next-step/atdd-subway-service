package nextstep.subway.path.domain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class PathFinder {
    private StationGraphStrategy stationGraphStrategy;

    public PathFinder(StationGraphStrategy stationGraphStrategy) {
        this.stationGraphStrategy = stationGraphStrategy;
    }

    public Path findShortestPath(List<Line> lines, Station target, Station source) {
        List<Section> sections = getSections(lines);

        validateSameSourceAndTarget(source, target);
        validateExistingStation(sections, source, target);

        return stationGraphStrategy.findShortestPath(sections, target, source);
    }

    private void validateSameSourceAndTarget(Station source, Station target) {
        if(source.equals(target)) {
            throw new PathException();
        }
    }

    private List<Section> getSections(List<Line> lines) {
        return lines.stream()
                .map(Line::getSections)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private void validateExistingStation(List<Section> sections, Station source, Station target) {
        if(isNotContain(sections, source) || isNotContain(sections, target)) {
            throw new PathException();
        }
    }

    private boolean isNotContain(List<Section> sections, Station station) {
        return !(sections.stream()
            .anyMatch(it -> it.isContain(station)));
    }
}
