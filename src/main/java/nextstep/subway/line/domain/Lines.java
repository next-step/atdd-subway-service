package nextstep.subway.line.domain;

import nextstep.subway.exception.LineHasNotExistShortestException;
import nextstep.subway.exception.StationNotExistException;
import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class Lines {
    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    public Line findShortestLine(ShortestDistance shortestDistance, Station source, Station target) {
        validateFindShortDistance(source, target);

        return lines.stream()
                .filter(item -> item.containsStationsExactly(source, target))
                .min((l1, l2) -> {
                    Distance l1Distance = l1.calcDistanceBetween(shortestDistance, source, target);
                    Distance l2Distance = l2.calcDistanceBetween(shortestDistance, source, target);

                    return l1Distance.compareTo(l2Distance);
                })
                .orElseThrow(() -> new LineHasNotExistShortestException("최단거리가 존재하지 않습니다."));
    }

    private void validateFindShortDistance(Station source, Station target) {
        final boolean notContainsStation = lines.stream()
                .noneMatch(item -> item.containsStationsExactly(source, target));
        if (notContainsStation) {
            throw new StationNotExistException("포함되지 않은 역이 있습니다.");
        }
    }

}
