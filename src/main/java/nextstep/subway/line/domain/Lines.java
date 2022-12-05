package nextstep.subway.line.domain;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Lines {
    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public int findMaxFare() {
        List<Fare> fares = lines.stream().map(Line::getFare).collect(toList());
        return Collections.max(fares).getFare();
    }

    public List<Line> getLines() {
        return Collections.unmodifiableList(lines);
    }
}
