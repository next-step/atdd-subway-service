package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayMap {
    private final List<Line> lines;

    public SubwayMap(List<Line> lines) {
        this.lines = lines;
    }

    public List<Station> getAllStations() {
        return lines.stream()
                .flatMap(line -> line.getStations().stream())
                .collect(Collectors.toList());
    }

    public List<Section> getAllSections() {
        return lines.stream()
                .map(Line::getSections)
                .flatMap(it -> it.getSections().stream())
                .collect(Collectors.toList());
    }
}
