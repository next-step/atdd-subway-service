package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Lines {
    private List<Line> lines;

    private Lines(List<Line> lines) {
        this.lines = lines;
    }

    public static Lines of(List<Line> lines) {
        return new Lines(lines);
    }

    public Lines getLinesFrom(List<Station> stations) {
        return Lines.of(lines
                .stream()
                .flatMap(line -> line.getSections().stream())
                .filter(section -> stations.contains(section.getUpStation()) && stations.contains(section.getDownStation()))
                .map(section -> section.getLine())
                .collect(Collectors.toList()));
    }

    public Line getMaxFareLine() {
        return lines.stream()
                .sorted((o1, o2) -> o1.getFare() - o2.getFare())
                .findFirst()
                .orElse(new Line());
    }
}
