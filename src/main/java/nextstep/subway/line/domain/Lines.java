package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import nextstep.subway.generic.domain.Price;
import nextstep.subway.station.domain.Station;

public class Lines {

    private final List<Line> items;

    public Lines(List<Line> items) {
        this.items = items;
    }

    public Lines(Set<Line> items) {
        this.items = new ArrayList<>(items);
    }

    public void foreach(Consumer<Line> consumer) {
        items.forEach(consumer);
    }

    public Sections bindDistance(Sections sections) {
        return new Sections(sections.getSections()
                .stream()
                .map(this::findSection)
                .collect(Collectors.toList()));
    }

    private Section findSection(Section section) {
        return items.stream()
                .filter(line -> line.hasSection(section))
                .findFirst()
                .map(line -> line.bindDistance(section))
                .orElseThrow(() -> new NoSuchElementException("구간을 찾을 수 없습니다."));
    }

    public boolean hasStation(Station station) {
        return items.stream()
                .anyMatch(line -> line.getStations().contains(station));
    }


    public Lines findLinesBySections(Sections sections) {
        return new Lines(sections.getSections()
                .stream()
                .filter(this::hasSection)
                .map(this::findLineBySection)
                .collect(Collectors.toSet()));
    }

    private Line findLineBySection(Section section) {
        return items.stream()
                .filter(line -> line.hasSection(section))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("노선을 찾을 수 업습니다."));
    }

    private boolean hasSection(Section section) {
        return items.stream()
                .anyMatch(line -> line.hasSection(section));
    }


    public Price maxSurcharge() {
        return items.stream()
                .max(Comparator.comparingInt(line -> line.getSurcharge().getValue()))
                .orElseThrow(() -> new NoSuchElementException("추가요금을 찾을 수 없습니다."))
                .getSurcharge();
    }
}
