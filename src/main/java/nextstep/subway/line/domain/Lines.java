package nextstep.subway.line.domain;

import nextstep.subway.fare.Fare;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Lines {
    private final List<Line> elements;

    public Lines(List<Line> lines) {
        elements = new ArrayList<>(lines);
    }

    public Fare getFare() {
     return elements.stream()
                .map(Line::getCharge)
                .max(Comparator.comparing(Fare::getValue))
                .orElseThrow(RuntimeException::new);
    }

    public List<Section> getAllSections() {
        return elements.stream()
                .map(Line::getSections)
                .map(Sections::getValues)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
