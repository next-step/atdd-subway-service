package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Lines {
    private final List<Line> values;

    public Lines(List<Line> lines) {
        values = new ArrayList<>(lines);
    }

    public List<Section> getAllSections() {
        return values.stream()
                .map(Line::getSections)
                .map(Sections::getValues)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
