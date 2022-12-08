package nextstep.subway.line.domain;

import java.util.Comparator;
import java.util.List;

public class Lines {
    private List<Line> lines;

    private Lines(List<Line> lines) {
        this.lines = lines;
    }

    public static Lines of(List<Line> lines) {
        return new Lines(lines);
    }

    public LineFare findMaxLineFare() {
        return lines.stream()
                .map(Line::getFare)
                .max(Comparator.comparing(it -> it))
                .orElse(LineFare.zero());
    }
}
