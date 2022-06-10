package nextstep.subway.line.domain;

import java.util.List;

public class Lines {
    private final List<Line> lines;

    private Lines(List<Line> lines) {
        this.lines = lines;
    }

    public static Lines valueOf(List<Line> lines) {
        return new Lines(lines);
    }

    public List<Line> lines() {
        return lines;
    }
}
