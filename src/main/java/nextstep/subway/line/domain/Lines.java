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
        List<Station> stations = line.stream()
            .flatMap(it -> it.getSections().unSortedStations().stream())
            .distinct()
            .collect(Collectors.toList());

        return Collections.unmodifiableList(stations);
    }

    public List<Section> getSectionsAll() {
        List<Section> sections = line.stream()
            .flatMap(it -> it.getSections().sortedSections().stream())
            .distinct()
            .collect(Collectors.toList());

        return Collections.unmodifiableList(sections);
    }

    public int size() {
        return line.size();
    }

}
