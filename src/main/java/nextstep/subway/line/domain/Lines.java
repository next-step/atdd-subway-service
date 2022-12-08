package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;

public class Lines {
    private List<Line> lines = new ArrayList<>();

    public Lines(List<Line> lines) {
        this.lines.addAll(lines);
    }

    public static Lines from(List<Line> lines) {
        return new Lines(lines);
    }

    public int getMaxAdditionalFare() {
        return lines.stream()
                .mapToInt(Line::getAdditionalFare)
                .max()
                .orElse(0);
    }
}
