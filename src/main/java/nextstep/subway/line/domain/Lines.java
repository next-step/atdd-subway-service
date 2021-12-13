package nextstep.subway.line.domain;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import nextstep.subway.station.domain.Station;

public class Lines {

    private List<Line> lines;

    private Lines(List<Line> lines) {
        this.lines = lines;
    }

    public static Lines of(List<Line> lines) {
        return new Lines(lines);
    }

    public List<Station> getAllStations() {
        return lines.stream()
            .map(Line::getStations)
            .flatMap(Collection::stream)
            .collect(toList());
    }

    public List<Section> getAllSections() {
        return lines.stream()
            .flatMap(line -> line.getSections().getSections().stream())
            .collect(toList());
    }

    public Lines getLinesInclude(List<Station> stations) {
        Set<Line> linesIncludePath = Sets.newHashSet();

        for (Line line : lines) {
            addLineIfIncludeStations(line, stations, linesIncludePath);
        }

        return Lines.of(Lists.newArrayList(linesIncludePath));
    }

    public Line mostExpensiveLine() {
        return lines.stream()
            .max(comparing(Line::getAdditionalFare))
            .orElseThrow(NoSuchElementException::new);
    }

    private void addLineIfIncludeStations(Line line, List<Station> stations,
        Set<Line> linesIncludePath) {
        for (int i = 0; i < stations.size() - 1; i++) {
            Station station = stations.get(i);
            Station nextStation = stations.get(i + 1);
            addIfIncludeStations(line, linesIncludePath, Lists.newArrayList(station, nextStation));
        }
    }

    private void addIfIncludeStations(Line line, Set<Line> linesIncludePath, List<Station> stations) {
        if (line.isContains(stations)) {
            linesIncludePath.add(line);
        }
    }

    public List<Line> getLines() {
        return lines;
    }
}
