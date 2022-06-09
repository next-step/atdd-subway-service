package nextstep.subway.line.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Lines {
    private List<Line> values;

    private Lines() {}

    private Lines(List<Line> values) {
        this.values = values;
    }

    public static Lines from(List<Line> values) {
        return new Lines(values);
    }

    public List<Line> getValues() {
        return this.values;
    }

    public int findLineFare(Sections fareSections) {
        List<Line> fareLines = this.values.stream()
                .filter(line -> line.hasFareSections(fareSections))
                .collect(Collectors.toList());

        return fareLines.stream()
                .mapToInt(line -> line.getFare().getValue())
                .max()
                .orElse(0);
    }
}
