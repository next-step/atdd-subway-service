package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Lines {
    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    public Line findShortestLine(Station source, Station target) {
        validateFindShortDistance(source, target);

        Optional<Line> minLine = lines.stream()
                .filter(item -> item.containsStationsExactly(source, target))
                .min((l1, l2) -> {
                    Distance l1Distance = l1.calcDistanceBetween(source, target);
                    Distance l2Distance = l2.calcDistanceBetween(source, target);

                    return l1Distance.compareTo(l2Distance);
                });

        return minLine.get();
    }

    private void validateFindShortDistance(Station source, Station target) {
        if (!lines.stream()
                .anyMatch(item -> item.containsStationsExactly(source, target))) {
            throw new IllegalArgumentException("포함되지 않은 역이 있습니다.");
        }
    }

}
