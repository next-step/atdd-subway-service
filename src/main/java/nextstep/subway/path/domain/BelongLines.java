package nextstep.subway.path.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

import static java.util.stream.Collectors.toList;

public class BelongLines {

    private final List<Line> lines;

    public BelongLines(List<Line> lines, List<Station> stations) {
        this.lines = getStationBelongLines(lines, stations);
    }

    public List<Line> getLines() {
        return lines;
    }

    public List<Line> getStationBelongLines(List<Line> lines, List<Station> stations) {

        Set<Line> lineSet = new HashSet<>();
        List<Section> sections = getAllSections(lines);

        for (int i = 0; i < stations.size() - 1; i++) {

            Station station = stations.get(i);
            Station nextStation = stations.get(i + 1);

            for (Section section : sections) {
                if (section.hasStations(station, nextStation)) {
                    lineSet.add(section.getLine());
                    break;
                }
            }
        }

        return new ArrayList<>(lineSet);
    }

    private List<Section> getAllSections(List<Line> lines) {
        return lines.stream()
                    .flatMap(line -> line.getSections().stream())
                    .collect(toList());
    }
}
