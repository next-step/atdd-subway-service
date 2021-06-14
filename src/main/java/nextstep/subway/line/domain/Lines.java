package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class Lines {
    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    public Line findShortDistance(Station source, Station target) {
        validateFindShortDistance(source, target);

        return null;
    }

    private void validateFindShortDistance(Station source, Station target) {
        if (!lines.stream()
                .anyMatch(item -> item.containsStationsExactly(source, target))) {
            throw new IllegalArgumentException("포함되지 않은 역이 있습니다.");
        }
    }

}
