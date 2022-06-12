package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Lines {
    private final List<Line> elements = new ArrayList<>();

    public Lines(List<Line> lines) {
        elements.addAll(lines);
    }

    public List<Station> getAllStations() {
        return elements.stream()
                .flatMap(line -> line.getStations().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Section> getAllSections() {
        return elements.stream()
                .flatMap(line -> line.getSections().stream())
                .collect(Collectors.toList());
    }
}
