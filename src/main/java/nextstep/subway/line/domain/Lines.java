package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class Lines {

    private final List<Line> lines = new ArrayList<>();

    protected Lines() {
    }

    public Lines(List<Line> lines) {
        this.lines.addAll(lines);
    }

    public static Lines of(List<Line> lines) {
        return new Lines(lines);
    }

    public static Lines empty() {
        return new Lines();
    }

    public void add(Line line) {
        this.lines.add(line);
    }

    public int getHighestExtraFare() {
        return lines.stream()
                .max(Comparator.comparing(Line::getExtraFare))
                .orElseThrow(NoSuchElementException::new)
                .getExtraFare();
    }
}
