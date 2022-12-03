package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Lines {

    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    public static Lines from(List<Line> lines) {
        return new Lines(lines);
    }

    public int maxExtraFare() {
        return lines.stream()
                .map(Line::getExtraFare)
                .max(Comparator.comparingInt(Fare::getValue))
                .orElse(Fare.free())
                .getValue();
    }
}
