package nextstep.subway.line.domain;

import java.util.List;

public class Lines {
    private List<Line> lines;

    private Lines() {
    }

    private Lines(List<Line> lines) {
        this.lines = lines;
    }
    
    public static Lines of(List<Line> lines) {
        return new Lines(lines);
    }

    public List<Line> getLines() {
        return lines;
    }
}
