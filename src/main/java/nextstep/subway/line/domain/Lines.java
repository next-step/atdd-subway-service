package nextstep.subway.line.domain;

import nextstep.subway.exception.NotExistMinimumFareLine;
import nextstep.subway.station.domain.Station;
import nextstep.subway.wrapped.Money;

import java.util.ArrayList;
import java.util.List;

public class Lines {
    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    public boolean containsStationsExactly(Station...stations) {
        boolean containsStations = lines.stream()
                .anyMatch(item -> item.containsStationsExactly(stations));

        return containsStations;
    }

    public Money findExpensiveFare() {
        return lines.stream()
                .map(Line::getMoney)
                .max(Money::compareTo)
                .orElseThrow(NotExistMinimumFareLine::new);
    }
}
