package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Lines {
    private final List<Line> lines;

    private Lines(List<Line> lines) {
        this.lines = lines;
    }

    public static Lines of(List<Line> lines) {
        return new Lines(lines);
    }

    private List<Line> findLinesByStations(List<Station> stations) {
        return lines
                .stream()
                .flatMap(line -> line.getSections().stream())
                .filter(section -> stations.contains(section.getUpStation()) && stations.contains(section.getDownStation()))
                .map(Section::getLine)
                .distinct()
                .collect(Collectors.toList());
    }

    public int getMaxFareByStations(List<Station> stations) {
        return findLinesByStations(stations)
                .stream().max(Comparator.comparingInt(Line::getFare))
                .map(line -> line.getFare())
                .orElse(0);
    }
}
