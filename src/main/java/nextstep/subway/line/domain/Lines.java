package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class Lines {

    private final List<Line> lines;

    private Lines(List<Line> lines) {
        this.lines = lines;
    }

    public static Lines from(List<Line> lines) {
        return new Lines(lines);
    }

    public List<Station> getStations() {
        return lines.stream()
                .flatMap(line -> line.getStations()
                        .stream())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Section> getSections() {
        return lines.stream()
                .flatMap(line -> line.getSections()
                        .stream())
                .collect(Collectors.toList());
    }
}
