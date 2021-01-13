package nextstep.subway.line.domain;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.station.domain.Station;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Lines {
    private static final int LOOP_START_IDX = 0;
    private static final int PADDING = 1;

    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public Set<Line> findThroughLines(List<Station> stations) {
        Set<Line> throughLines = new HashSet<>();
        int size = stations.size() - PADDING;
        Line foundLine = null;

        for ( int idx = LOOP_START_IDX ; idx < size ; idx ++ ) {
            Station departure = stations.get(idx);
            Station destination = stations.get(idx + PADDING);
            foundLine = findThroughLine(departure, destination, foundLine);
            throughLines.add(foundLine);
        }
        return throughLines;
    }

    private Line findThroughLine(Station departure, Station destination, Line prevFindLine) {
        if (prevFindLine == null || !prevFindLine.containsSection(departure, destination)) {
            return findThroughLine(departure, destination);
        }
        return prevFindLine;
    }

    private Line findThroughLine(Station departure, Station destination) {
        return lines.stream()
                .filter(it -> it.containsSection(departure, destination))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("해당 구간은 존재하지 않습니다 (" + departure.getName() + "->" + destination.getName() + ")"));
    }
}
