package nextstep.subway.path.domain;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.line.domain.Line;

import java.util.ArrayList;
import java.util.List;

public class ThroughLineSelector {
    private final List<Line> lines;
    private final List<Long> stationIds;

    private final List<Line> throughLines = new ArrayList<>();
    private Line prevFindLine = null;

    private boolean found = false;

    public ThroughLineSelector(List<Line> lines, List<Long> stationIds) {
        this.lines = lines;
        this.stationIds = stationIds;
    }

    public List<Line> find() {
        if (found) {
            return throughLines;
        }
        int size = stationIds.size() - 1;
        for ( int idx = 0 ; idx < size ; idx ++ ) {
            Long departureId = stationIds.get(idx);
            Long destinationId = stationIds.get(idx + 1);
            searchAndAdd(departureId, destinationId);
        }
        found = true;
        return throughLines;
    }

    private void searchAndAdd(Long departureId, Long destinationId) {
        if (prevFindLine == null || !prevFindLine.containsSection(departureId, destinationId)) {
            prevFindLine = findThroughLine(departureId, destinationId);
            throughLines.add(prevFindLine);
        }
    }

    private Line findThroughLine(Long departureId, Long destinationId) {
        return lines.stream()
                .filter(it -> it.containsSection(departureId, destinationId))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("해당 구간은 존재하지 않습니다 (" + departureId + "->" + destinationId + ")"));
    }
}
