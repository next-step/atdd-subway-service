package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

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
}
