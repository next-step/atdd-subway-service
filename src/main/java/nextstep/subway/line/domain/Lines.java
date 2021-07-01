package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.*;
import java.util.stream.Collectors;

public class Lines {
    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = Collections.unmodifiableList(lines);
    }

    public Station findStationById(Long id) {
        return lines.stream()
                .flatMap(line -> line.getStations().stream())
                .filter(station -> station.isSameId(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("없는 역입니다."));
    }

    public Set<Station> getStations() {
        return lines.stream()
                .flatMap(line -> line.getStations().stream())
                .collect(Collectors.toSet());
    }

    public List<Section> getSections() {
        return lines.stream()
                .flatMap(line -> line.getSections().stream())
                .collect(Collectors.toList());
    }

    public List<Line> getLines() {
        return new ArrayList<>(lines);
    }
}
