package nextstep.subway.line.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Lines {
    private List<Line> line;

    public Lines(List<Line> line) {
        this.line = line;
    }

    public static Lines of(List<Line> all) {
        return new Lines(all);
    }

    public List<Line> getLine() {
        return line;
    }
}
