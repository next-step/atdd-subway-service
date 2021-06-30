package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Lines {
    private final List<Line> line;

    public Lines(final List<Line> line) {
        this.line = line;
    }

    public List<Station> getStationAll() {
        List<Station> stations = collectItem(it -> it.getSections().unSortedStations().stream());
        return Collections.unmodifiableList(stations);
    }

    public List<Section> getSectionsAll() {
        List<Section> sections = collectItem(it -> it.getSections().sortedSections().stream());
        return Collections.unmodifiableList(sections);
    }

    public int size() {
        return line.size();
    }

    private <T> List<T> collectItem(Function<Line, Stream<? extends T>> mapper) {
        return line.stream()
            .flatMap(mapper)
            .distinct()
            .collect(Collectors.toList());
    }
}
