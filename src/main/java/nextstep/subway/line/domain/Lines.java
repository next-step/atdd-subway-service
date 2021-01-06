package nextstep.subway.line.domain;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.station.domain.Station;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-06
 */
public class Lines {

    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = Collections.unmodifiableList(lines);
    }

    public List<Station> getStations() {
        return lines.stream()
                .flatMap(line -> line.getStations().stream())
                .collect(Collectors.toList());
    }

    public List<Section> getSections() {
        return lines.stream()
                .flatMap(line -> line.getSections().stream())
                .collect(Collectors.toList());
    }

    public boolean contains(Station station) {
        return getStations().contains(station);
    }
}
