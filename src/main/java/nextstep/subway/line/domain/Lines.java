package nextstep.subway.line.domain;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.amount.domain.Amount;

public class Lines {
    private final List<Line> lines;

    private Lines(List<Line> lines) {
        this.lines = lines;
    }

    public static Lines from(List<Line> lines) {
        return new Lines(lines);
    }

    public Stations getStations() {
        return lines.stream()
            .map(Line::getStations)
            .reduce(Stations::mergeDistinct)
            .orElse(Stations.empty());
    }

    public Sections getSections() {
        return lines.stream()
            .map(Line::getSections)
            .reduce(Sections::merge)
            .orElse(Sections.empty());
    }

    public Amount maxAmount() {
        return lines.stream()
            .map(Line::getAmount)
            .max(Comparator.comparing(Amount::value))
            .orElse(Amount.empty());
    }

    public Lines findPathLines(Stations pathStations) {
        return Lines.from(
            this.lines.stream()
                .filter(line -> line.isPathLine(pathStations))
                .collect(Collectors.toList())
        );
    }

    public List<Line> getList() {
        return Collections.unmodifiableList(lines);
    }
}
