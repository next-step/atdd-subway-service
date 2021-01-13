package nextstep.subway.line.domain;

import nextstep.subway.exception.BadRequestException;

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

    public Set<Line> findThroughLines(List<Long> stationIds) {
        Set<Line> throughLines = new HashSet<>();
        int size = stationIds.size() - PADDING;
        Line foundLine = null;

        for ( int idx = LOOP_START_IDX ; idx < size ; idx ++ ) {
            Long departureId = stationIds.get(idx);
            Long destinationId = stationIds.get(idx + PADDING);
            foundLine = findThroughLine(departureId, destinationId, foundLine);
            throughLines.add(foundLine);
        }
        return throughLines;
    }

    private Line findThroughLine(Long departureId, Long destinationId, Line prevFindLine) {
        if (prevFindLine == null || !prevFindLine.containsSection(departureId, destinationId)) {
            return findThroughLine(departureId, destinationId);
        }
        return prevFindLine;
    }

    private Line findThroughLine(Long departureId, Long destinationId) {
        return lines.stream()
                .filter(it -> it.containsSection(departureId, destinationId))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("해당 구간은 존재하지 않습니다 (" + departureId + "->" + destinationId + ")"));
    }
}
