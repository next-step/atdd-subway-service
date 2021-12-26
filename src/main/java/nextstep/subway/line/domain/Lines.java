package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Lines {

    List<Line> lines = new ArrayList<>();

    public Lines() {
    }

    public Lines(final List<Line> lines) {
        this.lines = lines;
    }

    public List<Section> sections() {
        return lines
                .stream()
                .flatMap(line -> line.getSections().getSections().stream())
                .collect(Collectors.toList());
    }

    public Set<Station> stations() {
        return lines
                .stream()
                .flatMap(line -> line.getStations().stream())
                .collect(Collectors.toSet());
    }

}
