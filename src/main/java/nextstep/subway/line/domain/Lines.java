package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class Lines {

    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public List<Section> getAllSections() {
        return lines.stream()
                .flatMap(line -> line.getSectionList().stream())
                .collect(Collectors.toList());
    }

    public List<Station> getAllStations() {
        return lines.stream()
                .flatMap(line -> line.getSortedStation().stream())
                .collect(Collectors.toList());
    }
}
